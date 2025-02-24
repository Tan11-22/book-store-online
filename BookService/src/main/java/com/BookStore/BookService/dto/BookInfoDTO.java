package com.BookStore.BookService.dto;


import com.BookStore.BookService.model.Image;
import com.BookStore.BookService.model.Author;
import com.BookStore.BookService.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BookInfoDTO {

    private BookOverviewDTO bookOverviewDTO;

    private List<Author> authors;

    private  List<Category> categories;

    private List<Image> images;


}
