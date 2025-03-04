package com.BookStore.BookService.repository;


import com.BookStore.BookService.model.BookEntity;



import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, String> {
    // query thông tin sách dạng card dùng cho trang chủ , thể loại , tìm kiếm
    @Query(value = "{call SP_LAY_DS_SACH(:start, :size)}", nativeQuery = true)
    List<Map<String, Object>> layDSSach(@Param("start") int start, @Param("size") int size);


    @Query(value = "{call SP_LAY_DS_SACH_BAN_CHAY(:start, :size)}", nativeQuery = true)
    List<Map<String, Object>> layDSSachBanChay(@Param("start") int start, @Param("size") int size);

    @Query(value = "{call SP_TIM_SACH(:search, :start, :size)}", nativeQuery = true)
    List<Map<String, Object>> timSach(@Param("search") String search, @Param("start") int start, @Param("size") int size);

    @Query(value = "{call SP_COUNT_TIM_SACH(:search)}", nativeQuery = true)
    int demSachTimRa(@Param("search") String search);


    // những câu query để lấy thông tin chi tiết 1 cuốn sách
    @Query(value = "{call SP_LAY_CHI_TIET_SACH(:isbn)}", nativeQuery = true)
    Map<String, Object> layChiTietSach(@Param("isbn") String isbn);

    @Query(value = "{call SP_LAY_DS_HINH_ANH_SACH(:isbn)}", nativeQuery = true)
    List<Map<String,Object>> layDanhSachHinhAnhSach(@Param("isbn") String isbn);

    @Query(value = "{call SP_LAY_DS_TAC_GIA_SACH(:isbn)}", nativeQuery = true)
    List<Map<String,Object>> layDanhSachTacGiaSach(@Param("isbn") String isbn);

    @Query(value = "{call SP_LAY_DS_THE_LOAI_SACH(:isbn)}", nativeQuery = true)
    List<Map<String,Object>> layDanhSachTheLoaiSach(@Param("isbn") String isbn);
//

    @Query(value = "{call SP_LAY_TC_TG()}", nativeQuery = true)
    List<Map<String, Object>> layTCTG();

    @Query(value = "{call SP_LAY_TC_TL()}", nativeQuery = true)
    List<Map<String, Object>> layTCTL();


    @Query(value = "SELECT IDTHELOAI FROM THELOAISACH WHERE ISBN = :isbn", nativeQuery = true)
    List<Integer> getIdTheLoaiTT(@Param("isbn") String isbn);

    @Query(value = "{call SP_GET_BOOKDTO_BY_ISBN(:isbn)}", nativeQuery = true)
    Map<String, Object> getBookDtoByIsbn(@Param("isbn") String isbn);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name="jakarta.persistence.lock.timeout",value = "10000")})
    Optional<BookEntity> findBookEntityByIsbn(String isbn);
}
