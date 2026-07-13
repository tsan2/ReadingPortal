package ru.anastasya.readingportal.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.anastasya.readingportal.repositories.BookRepository;
import ru.anastasya.readingportal.repositories.ChapterRepository;
import ru.anastasya.readingportal.repositories.VolumeRepository;

@AllArgsConstructor
@Component("bookSecurity")
public class BookSecurity {

    private final BookRepository bookRepository;
    private final VolumeRepository volumeRepository;
    private final ChapterRepository chapterRepository;

    public boolean canCreateChapterPlaceHolder(Long bookId, Long volumeId, Long currentUserId){
        if (volumeId != null){
            return volumeRepository.existsByIdAndBookAuthorsId(volumeId, currentUserId);
        }
        else if (bookId != null) {
            return bookRepository.existsByIdAndAuthorsId(bookId, currentUserId);
        }
        return false;
    }

    public boolean checkAuthorityByChapterId(Long chapterId, Long currentUserId){
        return chapterRepository.existsByIdAndVolumeBookAuthorsId(chapterId, currentUserId);
    }

    public boolean checkAuthorityByVolumeId(Long volumeId, Long currentUserId){
        return volumeRepository.existsByIdAndBookAuthorsId(volumeId, currentUserId);
    }

    public boolean checkAuthorityByBookId(Long bookId, Long currentUserId){
        return bookRepository.existsByIdAndAuthorsId(bookId, currentUserId);
    }
}
