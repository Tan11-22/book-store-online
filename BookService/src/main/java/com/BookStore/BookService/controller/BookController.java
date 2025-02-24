package com.BookStore.BookService.controller;

import com.BookStore.BookService.dto.BookStoreResponse;
import com.BookStore.BookService.dto.BookItemDTO;
import com.BookStore.BookService.dto.BookInfoDTO;
import com.BookStore.BookService.dto.TacGiaDTO;
import com.BookStore.BookService.model.Category;
import com.BookStore.BookService.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sach-service/sach/")

public class BookController {
    @Autowired
    BookService sachService;

    @GetMapping("ds-sach")
    public BookStoreResponse<List<BookItemDTO>> layDSSach(@RequestParam("start") int start,
                                                          @RequestParam("size") int size) {
       return sachService.getListBookItems(start, size);
    }

    @GetMapping("ds-sach-ban-chay")
    public BookStoreResponse<List<BookItemDTO>> layDSSachBanChay(@RequestParam("start") int start,
                                                                 @RequestParam("size") int size) {
        return sachService.getBestSellingBooks(start, size);
    }


    @GetMapping("chi-tiet-sach")
    public BookStoreResponse<BookInfoDTO> layCTSach(@RequestParam("isbn") String isbn) {
        return sachService.getBookInfo(isbn);
    }


//    @GetMapping("tim-sach")
//    public BookStoreResponse<List<BookItemDTO>> layDSSach(@RequestParam("search") String search,
//                                                          @RequestParam("start") int start,
//                                                          @RequestParam("size") int size) {
//        return sachService.timSach(search, start, size);
//    }
//
//    @GetMapping("tong-sl-sach-tim")
//    public BookStoreResponse<Integer> layTongSLSachTim(@RequestParam("search") String search) {
//        return sachService.demSLSachTimRa(search);
//    }

    @PostMapping("search")
    public BookStoreResponse<List<BookItemDTO>> search(
            @RequestBody Map<String, Object> req
    ) {
        return sachService.searchBooks(req);
    }

    @PostMapping("search-amount")
    public BookStoreResponse<Integer> searchAmount(
            @RequestBody Map<String, Object> req
    ) {
        return sachService.countSearchResults(req);
    }

    @GetMapping("ds-tg")
    public BookStoreResponse<List<TacGiaDTO>> layThongTinTacGia() {
        return sachService.layTCTG();
    }

    @GetMapping("ds-tl")
    public BookStoreResponse<List<Category>> layThongTinTheLoai() {
        return sachService.layTCTL();
    }

    @GetMapping("sach-tuong-tu")
    public BookStoreResponse<List<BookItemDTO>> getSachCungTheLoai(
            @RequestParam("isbn") String isbn
    ) {
        return sachService.getBooksInSameCategory(isbn);
    }
}
