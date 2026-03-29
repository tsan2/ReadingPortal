package ru.anastasya.readingportal.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.services.BookService;
import ru.anastasya.readingportal.utils.JsonUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/book/*")
public class BookServlet extends HttpServlet {

    private static final BookService bookService = BookService.getInstance();
    private static final ObjectMapper jsonMapper = JsonUtil.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isBlank() || pathInfo.equals("/")){
            getBooks(req, resp);
        }
    }

    private void getBooks(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter respWriter = resp.getWriter();


        String[] authorsIdsParameter = req.getParameterValues("author_id");
        String[] genresIdsParameter = req.getParameterValues("genre_id");
        String sortStrategy = req.getParameter("sort_strategy");
        int page;
        int size;
        try {
            page = Integer.parseInt(req.getParameter("page"));
            size = Integer.parseInt(req.getParameter("size"));
        } catch (NumberFormatException e) {
            JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "неверный формат параметров page или size");
            return;
        }

        List<Long> authorsIds = new ArrayList<>();
        if (authorsIdsParameter != null){
            for (String id : authorsIdsParameter){
                try {
                    authorsIds.add(Long.parseLong(id));
                } catch (NumberFormatException e) {
                    JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "author_id может быть только числом");
                    return;
                }
            }
        }
        List<Long> genresIds = new ArrayList<>();
        if (genresIdsParameter != null){
            for (String id : genresIdsParameter){
                try {
                    genresIds.add(Long.parseLong(id));
                } catch (NumberFormatException e) {
                    JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "genre_id может быть только числом");
                    return;
                }
            }
        }

        BookSortStrategy bookSortStrategy = null;
        try{
            if (sortStrategy != null){
                bookSortStrategy = BookSortStrategy.valueOf(sortStrategy.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            //bookSortStrategy оставляем null
        }

        BookFilter bookFilter = new BookFilter(authorsIds, genresIds, bookSortStrategy, page, size);

        List<Book> books = bookService.findFullBooksByBookFilter(bookFilter);

        long totalCount = bookService.countBooksByBookFilter(bookFilter);

        List<BookResponseDTO> bookResponseDTOS = books.stream()
                .map(b -> {
                    List<AuthorShortDTO> authorShortDTOS = new ArrayList<>();
                    List<User> authors = b.getAuthors();
                    if (authors != null){
                        for (User user : authors){
                            authorShortDTOS.add(new AuthorShortDTO(user.getId(), user.getNickname()));
                        }
                    }

                    List<Genre> genres = new ArrayList<>();
                    if (b.getGenres() != null){
                        genres = b.getGenres();
                    }

                    return new BookResponseDTO(b.getId(), b.getTitle(), b.getDateChanged(),
                            b.getCreatedAt(), authorShortDTOS, genres);
                }).toList();


        PageResponseDTO<BookResponseDTO> pageResponseDTO = new PageResponseDTO<>(bookResponseDTOS, totalCount, size, page);

        String json = jsonMapper.writeValueAsString(pageResponseDTO);

        resp.setStatus(HttpServletResponse.SC_OK);
        respWriter.println(json);
    }

}
