package com.BookStore.BookService.service.impl;

import com.BookStore.BookService.dto.*;
import com.BookStore.BookService.model.Image;
import com.BookStore.BookService.model.Author;
import com.BookStore.BookService.model.Category;
import com.BookStore.BookService.repository.BookRepository;
import com.BookStore.BookService.service.BookService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private DataSource dataSource;
    Logger logger = LoggerFactory.getLogger(BookServiceImpl.class.getName());


    // query thông tin sách dạng card dùng cho trang chủ , thể loại , tìm kiếm
    @Override
    public BookStoreResponse<List<BookItemDTO>> getListBookItems(int start, int size) {
        List<Map<String, Object>> data = bookRepository.layDSSach(start, size);
        List<BookItemDTO> result = data.stream().map(map -> mapBookResultToDTO(map)).toList();
        return BookStoreResponse.<List<BookItemDTO>>builder()
                .code(200)
                .status("Lấy danh sách card sách thành công!")
                .data(result).build();
    }

    private BookItemDTO mapBookResultToDTO(Map<String, Object> data) {
        return BookItemDTO.builder()
                .isbn((String) data.get("ISBN"))
                .title((String) data.get("TENSACH"))
                .author((String) data.get("TENTACGIA"))
                .salePrice((Integer) data.get("GIABAN"))
                .discountPrice((Integer) data.get("GIAGIAM"))
                .image((String) data.get("TENANH"))
                .inventoryQuantity((Integer) data.get("SOLUONG"))
                .build();
    }


    // những câu query để lấy thông tin chi tiết 1 cuốn sách
    @Override
    public BookStoreResponse<BookInfoDTO> getBookInfo(String isbn) {
        BookInfoDTO bookInfo = findBookInfo(isbn);
        return BookStoreResponse.<BookInfoDTO>builder()
                .code(200)
                .status("Lấy thông tin chi tiết của sách thành công!")
                .data(bookInfo)
                .build();

    }

//    @Override
//    public BookStoreResponse<List<BookItemDTO>> timSach(String search, int start, int size) {
//        List<Map<String, Object>> data = bookRepository.timSach(search, start, size);
//        if (data.size() == 0) {
//            return BookStoreResponse.<List<BookItemDTO>>builder()
//                    .code(201)
//                    .status("Không tìm thấy sách phù hợp!")
//                    .data(null).build();
//        }
//        List<BookItemDTO> result = data.stream().map(map -> mapBookResultToDTO(map)).toList();
//        return BookStoreResponse.<List<BookItemDTO>>builder()
//                .code(200)
//                .status("Lấy danh sách card sách thành công!")
//                .data(result).build();
//    }

//    @Override
//    public BookStoreResponse demSLSachTimRa(String search) {
//        return BookStoreResponse.<Integer>builder()
//                .code(200)
//                .status("Lấy tổng số lượng thành công!")
//                .data(bookRepository.demSachTimRa(search)).build();
//    }

    @Override
    public BookStoreResponse getBestSellingBooks(int start, int size) {
        List<Map<String, Object>> data = bookRepository.layDSSachBanChay(start, size);
        List<BookItemDTO> result = data.stream().map(map -> mapBookResultToDTO(map)).toList();
        return BookStoreResponse.<List<BookItemDTO>>builder()
                .code(200)
                .status("Lấy danh sách card sách thành công!")
                .data(result).build();
    }

    private BookInfoDTO findBookInfo(String isbn) {
        BookOverviewDTO bookOverviewDTO = mapBookToBOverviewDTO(bookRepository.layChiTietSach(isbn));
        List<Image> images = mapObjectToImage(isbn);
        List<Author> authors = mapObjectToAuthor(isbn);
        List<Category> categories = mapObjectToCategory(isbn);

        return BookInfoDTO.builder()
                .bookOverviewDTO(bookOverviewDTO)
                .images(images)
                .authors(authors)
                .categories(categories)
                .build();
    }

    private BookOverviewDTO mapBookToBOverviewDTO(Map<String, Object> data) {
        return BookOverviewDTO.builder()
                .isbn((String) data.get("ISBN"))
                .title((String) data.get("TENSACH"))
                .formatSize((String) data.get("KHUONKHO"))
                .pageCount((Integer) data.get("SOTRANG"))
                .weight((Integer) data.get("TRONGLUONG"))
                .description((String) data.get("MOTA"))
                .quantity((Integer) data.get("SOLUONG"))
                .publisherCode((String) data.get("MANHAXUATBAN"))
                .publisherName((String) data.get("TENNHAXUATBAN"))
                .salePrice((Integer) data.get("GIABAN"))
                .discountPrice((Integer) data.get("GIAGIAM"))
                .reviewCount((Integer) data.get("SOBINHLUAN"))
                .totalRating((Integer) data.get("TONGDIEM"))
                .build();
    }



    private List<Author> mapObjectToAuthor(String isbn) {
        List<Map<String, Object>> data = bookRepository.layDanhSachTacGiaSach(isbn);
        return data.stream().map(map ->
                Author.builder()
                        .idTacGia((Integer) map.get("IDTACGIA"))
                        .ho((String) map.get("HO"))
                        .ten((String) map.get("TEN"))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<Category> mapObjectToCategory(String isbn) {
        List<Map<String, Object>> data = bookRepository.layDanhSachTheLoaiSach(isbn);
        return data.stream().map(map ->
                Category.builder()
                        .idTheLoai((Integer) map.get("IDTHELOAI"))
                        .tenTheLoai((String) map.get("TENTHELOAI"))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<Image> mapObjectToImage(String isbn) {
        List<Map<String, Object>> data = bookRepository.layDanhSachHinhAnhSach(isbn);
        return data.stream().map(map ->
                Image.builder()
                        .idAnh((Integer) map.get("IDANH"))
                        .isbn((String) map.get("ISBN"))
                        .filename((String) map.get("FILENAME"))
                        .build()
        ).collect(Collectors.toList());
    }


    private List<BookItemDTO> searchAdvancedBooks(String query,
                                             List<String> tacGiaIDs,
                                             List<String> theLoaiIDs,
                                             int sapXep,
                                             int start,
                                             int size
    ) {
        List<BookItemDTO> books = new ArrayList<>();
        String find = "SELECT * FROM SACH WHERE";
        String find1 = " ISBN LIKE N'%" + query + "%' OR TENSACH LIKE N'%" + query + "%' OR MOTA LIKE N'%" + query + "%' ";
        String find2 = "";
        String find3 = "";
        if (tacGiaIDs.size() == 0||tacGiaIDs.get(0).equals("")) {
            find2 +=
                    " ISBN IN " +
                            "(SELECT ISBN FROM (SELECT * FROM TACGIA WHERE HO + ' ' + TEN LIKE N'%" + query + "%')" +
                            " TG INNER JOIN (SELECT * FROM SANGTAC) ST ON ST.IDTACGIA = TG. IDTACGIA)"
            ;
        } else {
            find2 +=
                    " ISBN IN (SELECT ISBN FROM SANGTAC WHERE IDTACGIA=" + tacGiaIDs.get(0);
            for (int i = 1; i < tacGiaIDs.size(); i++) {
                find2 += " OR IDTACGIA=" + tacGiaIDs.get(i);
            }
            find2 += " GROUP BY ISBN HAVING COUNT(DISTINCT IDTACGIA) >=" + tacGiaIDs.size() + ") ";

        }


        if (theLoaiIDs.size() == 0||theLoaiIDs.get(0).equals("")) {
            find3 +=
                    " ISBN IN " +
                            "(SELECT ISBN FROM (SELECT * FROM THELOAI WHERE TENTHELOAI LIKE N'%" + query + "%')" +
                            " TL INNER JOIN (SELECT * FROM THELOAISACH) TLS ON TL.IDTHELOAI = TLS.IDTHELOAI)"
            ;
        } else {
            find3 +=
                    " ISBN IN (SELECT ISBN FROM THELOAISACH WHERE IDTHELOAI=" + theLoaiIDs.get(0);
            for (int i = 1; i < theLoaiIDs.size(); i++) {
                find3 += " AND IDTHELOAI=" + theLoaiIDs.get(i);
            }
            find3 += " GROUP BY ISBN HAVING COUNT(DISTINCT IDTHELOAI) >=" + theLoaiIDs.size() + ") ";

        }

        if (!query.equals("")) {
            find += find1 + " OR " + find2 + " OR " + find3;
        } else {
            find += (tacGiaIDs.size() != 0 && theLoaiIDs.size() != 0) ? find2 + " OR " + find3
                    : (tacGiaIDs.size() != 0 ? find2 : find3);
        }
//        logger.error("check log offset:");
//        logger.error(String.valueOf(start));
//        logger.error(String.valueOf(size));
        String sql = "DECLARE @ngayhientai DATE ; SET @ngayhientai = CAST(GETDATE() AS DATE);" +
                "SELECT S.ISBN, S.TENSACH, TENTACGIA = TG.HO + ' ' + TG.TEN, " +
                " CTGS.GIA AS GIABAN, CTGS1.GIA AS GIAGIAM, " +
                " HA.FILENAME AS TENANH, S.SOLUONG  FROM (" +
                find +   // câu lệnh query tìm kiếm
                " ) S INNER JOIN ( SELECT * FROM CTGIASACH WHERE IDGIA = '2' AND @ngayhientai BETWEEN NGAYAPDUNG AND NGAYKETTHUC" +
                ") CTGS " +
                " ON CTGS.ISBN = S.ISBN" +
                " LEFT JOIN ( SELECT * FROM CTGIASACH WHERE IDGIA = '3' AND @ngayhientai BETWEEN NGAYAPDUNG AND NGAYKETTHUC" +
                " ) CTGS1 ON CTGS1.ISBN = S.ISBN LEFT JOIN (" +
                "SELECT * FROM HINHANH WHERE IDANH IN ( SELECT MIN(IDANH) FROM HINHANH GROUP BY ISBN)" +
                ") HA ON HA.ISBN = S.ISBN LEFT JOIN ( SELECT *" +
                " FROM SANGTAC WHERE IDSANGTAC IN (SELECT MIN(IDSANGTAC) FROM SANGTAC GROUP BY ISBN)" +
                ") ST ON ST.ISBN = S.ISBN INNER JOIN (" +
                "SELECT * FROM TACGIA) TG ON TG.IDTACGIA = ST.IDTACGIA"
                +" ORDER BY GIABAN " + (sapXep == 0? "ASC":"DESC") // ASC tăng dần , desc giảm dần
                + " OFFSET ("+String.valueOf(start)+") ROWS FETCH NEXT ("+String.valueOf(size)+") ROWS ONLY;"
                ;


        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BookItemDTO book = new BookItemDTO();
                book.setIsbn(rs.getString("ISBN"));
                book.setTitle(rs.getString("TENSACH"));
                book.setAuthor(rs.getString("TENTACGIA"));
                book.setSalePrice(rs.getInt("GIABAN"));
                book.setDiscountPrice(rs.getInt("GIAGIAM"));
                book.setImage(rs.getString("TENANH"));
                book.setInventoryQuantity(rs.getInt("SOLUONG"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Integer demSoLuongSachTimNangCao(String query,
                                             List<String> tacGiaIDs,
                                             List<String> theLoaiIDs) {
//        List<CardSach> books = new ArrayList<>();
//        System.out.println(tacGiaIDs);
//        System.out.println(tacGiaIDs.get(0));
//        System.out.println(theLoaiIDs);
        Integer result = 0;
        String find = "SELECT * FROM SACH WHERE";
        String find1 = " ISBN LIKE N'%" + query + "%' OR TENSACH LIKE N'%" + query + "%' OR MOTA LIKE N'%" + query + "%' ";
        String find2 = "";
        String find3 = "";
        if (tacGiaIDs.size() == 0||tacGiaIDs.get(0).equals("")) {
            find2 +=
                    " ISBN IN " +
                            "(SELECT ISBN FROM (SELECT * FROM TACGIA WHERE HO + ' ' + TEN LIKE N'%" + query + "%')" +
                            " TG INNER JOIN (SELECT * FROM SANGTAC) ST ON ST.IDTACGIA = TG. IDTACGIA)"
            ;
        } else {
            find2 +=
                    " ISBN IN (SELECT ISBN FROM SANGTAC WHERE IDTACGIA=" + tacGiaIDs.get(0);
            for (int i = 1; i < tacGiaIDs.size(); i++) {
                find2 += " OR IDTACGIA=" + tacGiaIDs.get(i);
            }
            find2 += " GROUP BY ISBN HAVING COUNT(DISTINCT IDTACGIA) >=" + tacGiaIDs.size() + ") ";

        }


        if (theLoaiIDs.size() == 0||theLoaiIDs.get(0).equals("")) {
            find3 +=
                    " ISBN IN " +
                            "(SELECT ISBN FROM (SELECT * FROM THELOAI WHERE TENTHELOAI LIKE N'%" + query + "%')" +
                            " TL INNER JOIN (SELECT * FROM THELOAISACH) TLS ON TL.IDTHELOAI = TLS.IDTHELOAI)"
            ;
        } else {
            find3 +=
                    " ISBN IN (SELECT ISBN FROM THELOAISACH WHERE IDTHELOAI=" + theLoaiIDs.get(0);
            for (int i = 1; i < theLoaiIDs.size(); i++) {
                find3 += " AND IDTHELOAI=" + theLoaiIDs.get(i);
            }
            find3 += " GROUP BY ISBN HAVING COUNT(DISTINCT IDTHELOAI) >=" + theLoaiIDs.size() + ") ";

        }

        if (!query.equals("")) {
            find += find1 + " OR " + find2 + " OR " + find3;
        } else {
            find += (tacGiaIDs.size() != 0 && theLoaiIDs.size() != 0) ? find2 + " OR " + find3
                    : (tacGiaIDs.size() != 0 ? find2 : find3);
        }
        String sql = "DECLARE @ngayhientai DATE ; SET @ngayhientai = CAST(GETDATE() AS DATE);" +
                "SELECT COUNT(S.ISBN)  FROM (" +
                find +   // câu lệnh query tìm kiếm
                " ) S INNER JOIN ( SELECT * FROM CTGIASACH WHERE IDGIA = '2' AND @ngayhientai BETWEEN NGAYAPDUNG AND NGAYKETTHUC" +
                ") CTGS " +
                " ON CTGS.ISBN = S.ISBN "+
                "INNER JOIN (SELECT * FROM SANGTAC WHERE IDSANGTAC IN ( SELECT MIN(IDSANGTAC)FROM SANGTAC GROUP BY ISBN)) ST ON ST.ISBN = S.ISBN"
                ;
//        System.out.println(sql);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if(rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public BookStoreResponse<List<BookItemDTO>> searchBooks(Map<String, Object> data) {
        String search = (String)data.get("search");
        List<String> authorIDs= (List<String>) data.get("tacGiaIDs");
        List<String> categoryIDs =  (List<String>) data.get("theLoaiIDs");
        Integer sapXep = (Integer) data.get("sapXep");
        Integer start = (Integer) data.get("start");
        Integer size = (Integer) data.get("size");
        List<BookItemDTO> books = searchAdvancedBooks(search, authorIDs, categoryIDs,sapXep, start, size);
        return BookStoreResponse.<List<BookItemDTO>>builder()
                .code(200)
                .status("")
                .data(books).build();
    }

    public BookStoreResponse<Integer> countSearchResults(Map<String, Object> data) {
        String search = (String)data.get("search");
        List<String> authorIDs= (List<String>) data.get("tacGiaIDs");
        List<String> categoryIDs =  (List<String>) data.get("theLoaiIDs");
        Integer count = demSoLuongSachTimNangCao(search, authorIDs, categoryIDs);
        return BookStoreResponse.<Integer>builder()
                .code(200)
                .status("")
                .data(count).build();
    }


    private TacGiaDTO mapDataToTG(Map<String, Object> data) {
        return TacGiaDTO.builder()
                .idTacGia((Integer) data.get("IDTACGIA"))
                .hoTen((String) data.get("HO")+" "+ (String) data.get("TEN"))
                .build();
    }



    private Category mapDataToTL(Map<String, Object> data) {
        return Category.builder()
                .idTheLoai((Integer) data.get("IDTHELOAI"))
                .tenTheLoai((String) data.get("TENTHELOAI"))
                .build();
    }

    @Override
    public BookStoreResponse<List<TacGiaDTO>> layTCTG() {
        List<TacGiaDTO> result = bookRepository.layTCTG().stream().map(map -> mapDataToTG(map)).toList();
        return BookStoreResponse.<List<TacGiaDTO>>builder()
                .code(200)
                .status("Lấy danh sách tác giả thành công!")
                .data(result)
                .build();
    }

    @Override
    public BookStoreResponse<List<Category>> layTCTL() {
        List<Category> result = bookRepository.layTCTL().stream().map(map -> mapDataToTL(map)).toList();
        return BookStoreResponse.<List<Category>>builder()
                .code(200)
                .status("Lấy danh sách thể loại thành công!")
                .data(result)
                .build();
    }

    private List<BookItemDTO> getSimilarBooks (String isbn) {
        List<BookItemDTO> books = new ArrayList<>();
        List<Integer> idTheLoais = bookRepository.getIdTheLoaiTT(isbn);
        System.out.println(idTheLoais);
        String find = "SELECT * FROM SACH WHERE";
//        String find1 = " ISBN LIKE '%" + query + "%' OR TENSACH LIKE '%" + query + "%'";
        find += " ISBN IN (SELECT ISBN FROM THELOAISACH WHERE ISBN !='"+isbn+"' AND IDTHELOAI=" + idTheLoais.get(0);
            for (int i = 1; i < idTheLoais.size(); i++) {
                find += " OR IDTHELOAI=" + idTheLoais.get(i);
            }
        find += ") ";
//        logger.error(find);
        String sql = "DECLARE @ngayhientai DATE ; SET @ngayhientai = CAST(GETDATE() AS DATE);" +
                " SELECT TOP 3 S.ISBN, S.TENSACH, TENTACGIA = TG.HO + ' ' + TG.TEN, " +
                " CTGS.GIA AS GIABAN, CTGS1.GIA AS GIAGIAM, " +
                " HA.FILENAME AS TENANH  FROM (" +
                find +   // câu lệnh query tìm kiếm
                " ) S INNER JOIN ( SELECT * FROM CTGIASACH WHERE IDGIA = '2' AND @ngayhientai BETWEEN NGAYAPDUNG AND NGAYKETTHUC" +
                ") CTGS " +
                " ON CTGS.ISBN = S.ISBN" +
                " LEFT JOIN ( SELECT * FROM CTGIASACH WHERE IDGIA = '3' AND @ngayhientai BETWEEN NGAYAPDUNG AND NGAYKETTHUC" +
                " ) CTGS1 ON CTGS1.ISBN = S.ISBN LEFT JOIN (" +
                "SELECT * FROM HINHANH WHERE IDANH IN ( SELECT MIN(IDANH) FROM HINHANH GROUP BY ISBN)" +
                ") HA ON HA.ISBN = S.ISBN LEFT JOIN ( SELECT *" +
                " FROM SANGTAC WHERE IDSANGTAC IN (SELECT MIN(IDSANGTAC) FROM SANGTAC GROUP BY ISBN)" +
                ") ST ON ST.ISBN = S.ISBN INNER JOIN (" +
                "SELECT * FROM TACGIA) TG ON TG.IDTACGIA = ST.IDTACGIA"
               + " ORDER BY GIABAN ASC"
                ;
//        logger.info(sql);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BookItemDTO book = new BookItemDTO();
                book.setIsbn(rs.getString("ISBN"));
                book.setTitle(rs.getString("TENSACH"));
                book.setAuthor(rs.getString("TENTACGIA"));
                book.setSalePrice(rs.getInt("GIABAN"));
                book.setDiscountPrice(rs.getInt("GIAGIAM"));
                book.setImage(rs.getString("TENANH"));
                book.setInventoryQuantity(rs.getInt("SOLUONG"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    @Override
    public BookStoreResponse<List<BookItemDTO>> getBooksInSameCategory(String isbn) {
        List<BookItemDTO> books = getSimilarBooks (isbn);
        return BookStoreResponse.<List<BookItemDTO>>builder()
                .code(200)
                .status("Lấy danh sách sách cùng thể loại thành công!")
                .data(books).build();
    }
    @Override
    @Cacheable(value = "books", key = "#isbn")
    public BookDto getBookDtoByIsbn(String isbn) {
        Map<String, Object> book = bookRepository.getBookDtoByIsbn(isbn);
        return BookDto.builder()
                .isbn(isbn)
                .title((String) book.get("TITLE"))
                .salePrice((Integer) book.get("SALE_PRICE"))
                .discountPrice((Integer) book.get("DISCOUNT_PRICE"))
                .image((String) book.get("IMAGE"))
                .pageCount((Integer) book.get("PAGE_COUNT"))
                .weight((Integer) book.get("WEIGHT"))
                .inventoryQuantity((Integer) book.get("INVENTORY_QUANTITY"))
                .build();
    }
    
}
