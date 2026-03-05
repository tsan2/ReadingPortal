package ru.anastasya.readingportal.ui;

import ru.anastasya.readingportal.dao.*;
import ru.anastasya.readingportal.dto.BookFilter;
import ru.anastasya.readingportal.dto.BookSortStrategy;
import ru.anastasya.readingportal.dto.ChapterCreateDTO;
import ru.anastasya.readingportal.dto.FractionalNumber;
import ru.anastasya.readingportal.exception.AuthorizationException;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.models.*;
import ru.anastasya.readingportal.services.*;
import ru.anastasya.readingportal.utils.OperationResult;
import ru.anastasya.readingportal.utils.SessionContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args){
        BookDAO bookDAO = new BookDAO();
        ChapterDAO chapterDAO = new ChapterDAO();
        GenreDAO genreDAO = new GenreDAO();
        PasswordResetCodeDAO resetCodeDAO = new PasswordResetCodeDAO();
        UserDAO userDAO = new UserDAO();
        VolumeDAO volumeDAO = new VolumeDAO();

        VolumeService volumeService = new VolumeService(volumeDAO, bookDAO);
        ChapterService chapterService = new ChapterService(chapterDAO, volumeService, bookDAO);
        BookService bookService = new BookService(bookDAO, userDAO, genreDAO, chapterService, volumeService);
        EmailService emailService = new EmailService();
        GenreService genreService = new GenreService(genreDAO);
        PasswordResetCodeService resetCodeService = new PasswordResetCodeService(resetCodeDAO, emailService, userDAO);
        UserService userService = new UserService(userDAO, resetCodeService, bookService);

        try(Scanner sc = new Scanner(System.in)) {
            boolean exit = false;
            do {
                System.out.println("Выберите, что вы хотите сделать(введите номер):");
                System.out.println("1. Регистрация"); //
                System.out.println("2. Авторизация"); //
                System.out.println("3. Мой профиль"); //
                System.out.println("4. Посмотреть все мои книги"); //
                System.out.println("5. Создание книги"); //
                System.out.println("6. Добавление тома"); //
                System.out.println("7. Добавление главы"); //
                //System.out.println("8. Изменить главу"); //отложим по причине того что надо и изменить главу и том и книгу
                //что название или текст
                System.out.println("8. Добавить жанр к книге"); //
                System.out.println("9. Добавить автора к книге"); //
                System.out.println("10. Поиск книг"); //
                //потом добавить вопрос какого автора жанр сортировка и т.д
                System.out.println("11. Открыть книгу"); //
                System.out.println("12. Удалить книгу"); //
                System.out.println("13. Удалить том"); //
                System.out.println("14. Удалить главу"); //
                System.out.println("15. Сменить пароль или никнейм"); //
                System.out.println("16. Выйти из аккаунта"); //
                System.out.println("17. Удалить аккаунт"); //
                System.out.println("18. Выйти из приложения");

                int num = sc.nextInt();
                sc.nextLine();

                switch (num){
                    case 1:
                        register(userService, sc);
                        break;
                    case 2:
                        login(userService, sc);
                        break;
                    case 3:
                        findMyProfile();
                        break;
                    case 4:
                        findAllMyBook(bookService);
                        break;
                    case 5:
                        createBook(bookService, sc);
                        break;
                    case 6:
                        addVolume(volumeService, bookService, sc);
                        break;
                    case 7:
                        addChapter(chapterService, volumeService, bookService, sc);
                        break;
                    case 8:
                        addGenreToBook(bookService, genreService, sc);
                        break;
                    case 9:
                        addAuthorToBook(bookService, userService, sc);
                        break;
                    case 10:
                        findBooks(bookService, userService, genreService, sc);
                        break;
                    case 11:
                        openBook(bookService, userService, genreService, volumeService, chapterService, sc);
                        break;
                    case 12:
                        deleteBook(bookService, sc);
                        break;
                    case 13:
                        deleteVolume(volumeService, bookService, sc);
                        break;
                    case 14:
                        deleteChapter(chapterService, volumeService, bookService, sc);
                        break;
                    case 15:
                        changeUserInfo(userService, resetCodeService, sc);
                        break;
                    case 16:
                        logout();
                        break;
                    case 17:
                        deleteAccount(userService, sc);
                        break;
                    case 18:
                        exit = true;
                        break;
                    default:
                        System.out.println("Введен неверный номер пункта");
                        break;

                }
            } while (!exit);
        }
    }

    //1
    public static void register(UserService userService, Scanner sc){
        System.out.println("Зарегистрируйтесь");
        System.out.println("Введите имя");
        String nickname = sc.nextLine();
        //добавить проверку на корректность почты ещё и в коде
        System.out.println("Введите почту");
        String email = sc.nextLine();
        System.out.println("Введите пароль");
        String password = sc.nextLine();
        User user = new User(nickname, email, password);
        userService.registerUser(user);
        System.out.println("Вы зарегистрировались");

    }

    //2
    public static void login(UserService userService, Scanner sc){
        System.out.println("Авторизуйтесь");
        System.out.println("Введите никнейм или почту");
        String nicknameOrEmail = sc.nextLine();
        System.out.println("Введите пароль");
        String password = sc.nextLine();
        User user = userService.authorizationUser(nicknameOrEmail, password);
        SessionContext.login(user);
        System.out.println("Вы вошли в аккаунт - " + user.getNickname());
    }

    //3
    public static void findMyProfile(){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User user = SessionContext.getUser();
        System.out.println("Ваш профиль");
        System.out.println("Никнейм - " + user.getNickname());
        System.out.println("Почта - " + user.getEmail());
        System.out.println("Аккаунт создан - " + user.getCreatedAt().toLocalDate());
    }

    //4
    public static void findAllMyBook(BookService bookService){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();
        BookFilter bookFilter = new BookFilter(currentUser.getId(), null, null);
        List<Book> books = bookService.findFullBooksByBookFilter(bookFilter);

        if (books.isEmpty()){
            System.out.println("У вас ещё нет книг");
            return;
        }
        System.out.println("Ваши книги");
        for (Book book : books){
            System.out.print(book.getId() + " " + book.getTitle() + " " + book.getCreatedAt().toLocalDate() + " ");
            if (book.getGenres() != null){
                for (Genre genre : book.getGenres()){
                    System.out.print("Жанры - ");
                    System.out.print(genre.getName() + " ");
                }
            }
            System.out.println();
        }
    }

    //вспомогательные
    public static void findAllVolumes(Long bookId, VolumeService volumeService){
        List<Volume> volumes = volumeService.findAllByBookId(bookId);

        for (Volume volume : volumes){
            FractionalNumber fractionalNumber = new FractionalNumber(volume.getVolumeMainNumber(), volume.getVolumeSubNumber());
            double number = mapToDouble(fractionalNumber);
            System.out.println(volume.getId() + " " + volume.getTitle() + " " + number);
        }

    }

    public static void findAllChapters(Long volumeId, ChapterService chapterService){
        List<Chapter> chapters = chapterService.findAllByVolumeId(volumeId);

        for (Chapter chapter : chapters){
            FractionalNumber fractionalNumber = new FractionalNumber(chapter.getChapterMainNumber(), chapter.getChapterSubNumber());
            double number = mapToDouble(fractionalNumber);
            System.out.println(chapter.getId() + " " + chapter.getTitle() + " " + number);
        }
    }

    public static void findAllGenres(GenreService genreService){
        List<Genre> genres = genreService.findAll();

        for (Genre genre : genres){
            System.out.println(genre.getId() + " " + genre.getName());
        }
    }

    //5
    public static void createBook(BookService bookService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        System.out.println("Создаем книгу");
        System.out.println("Введите название:");
        String title = sc.nextLine();
        Book book = new Book(title);
        Long bookId = bookService.createBookPlaceholder(book, currentUser.getId());
        System.out.println("Книга создана");
    }

    //6
    public static void addVolume(VolumeService volumeService, BookService bookService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        findAllMyBook(bookService);
        System.out.println("Выберите книгу, к которой хотите добавить том(Введите id):");

        Long bookId = sc.nextLong();

        sc.nextLine();
        System.out.println("Создаем том");
        System.out.println("Введите название:");
        String title = sc.nextLine();
        System.out.println("Введите номер тома (Если дробное число, то ',', а не '.'):");
        double number = sc.nextDouble();
        FractionalNumber fractionalNumber = mapToFractionalNumber(number);

        Volume volume = new Volume(title, fractionalNumber.mainNumber(), fractionalNumber.SubNumber(), bookId);

        volumeService.createVolume(volume, currentUser.getId());
        System.out.println("Том создан");
    }

    //7
    public static void addChapter(ChapterService chapterService, VolumeService volumeService, BookService bookService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        findAllMyBook(bookService);
        System.out.println("Выберите книгу, к которой хотите добавить главу(Введите id):");

        Long bookId = sc.nextLong();

        sc.nextLine();
        Long volumeId = null;
        if (volumeService.existsNotDefaultVolume(bookId)){
            findAllVolumes(bookId, volumeService);
            System.out.println("Выберите том, к которому хотите добавить главу(Введите id):");
            volumeId = sc.nextLong();
            sc.nextLine();
        }

        System.out.println("Создаем главу");
        System.out.println("Введите название:");
        String title = sc.nextLine();

        System.out.println("Введите номер главы (Если дробное число, то ',', а не '.'):");
        double number = sc.nextDouble();
        FractionalNumber fractionalNumber = mapToFractionalNumber(number);

        System.out.println("Введите текст главы (Чтобы закончить ввод введите единственное слово exit в строке):");
        StringBuilder content = new StringBuilder();
        String line = sc.nextLine();
        while (!line.equalsIgnoreCase("exit")){
            content.append(line);
            content.append("\n");
            line = sc.nextLine();
        }

        ChapterCreateDTO createDTO = new ChapterCreateDTO(bookId, title, fractionalNumber.mainNumber(), fractionalNumber.SubNumber(), volumeId);
        OperationResult operationResult = chapterService.createChapterPlaceHolder(createDTO, currentUser.getId());
        chapterService.addContent(operationResult.getIdCreated(), content.toString(), currentUser.getId());

        System.out.println("Глава создана");
    }

    //8
    public static void addGenreToBook(BookService bookService, GenreService genreService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        findAllMyBook(bookService);
        System.out.println("Выберите к какой книге вы хотите добавить жанр(Введите id):");

        Long bookId = sc.nextLong();

        sc.nextLine();
        findAllGenres(genreService);
        System.out.println("Выберите жанр(Введите id):");
        Long genreId = sc.nextLong();
        sc.nextLine();
        bookService.addGenreToBook(bookId, genreId, currentUser.getId());
        System.out.println("Жанр добавлен");
    }

    //9
    public static void addAuthorToBook(BookService bookService, UserService userService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();
        findAllMyBook(bookService);
        System.out.println("Выберите к какой книге вы хотите добавить автора(Введите id):");

        Long bookId = sc.nextLong();

        sc.nextLine();
        System.out.println("Введите никнейм автора, которого хотите добавить:");
        String authorNickname = sc.nextLine();
        User author = userService.findUserByNickname(authorNickname);

        bookService.addAuthorToBook(bookId, author.getId(), currentUser.getId());
        System.out.println("Автор добавлен");
    }

    //10
    public static void findBooks(BookService bookService, UserService userService, GenreService genreService, Scanner sc){
        BookFilter bookFilter = new BookFilter(null, null, null);

        int num;
        do{
            System.out.println("Настройка вывода информации");
            System.out.println("1. Выбор конкретного автора");
            System.out.println("2. Выбор конкретного жанра");
            System.out.println("3. Сортировка");
            System.out.println("4. Закончить настройку и вывести список книг");
            System.out.println("Введите номер пункта:");
            num = sc.nextInt();
            sc.nextLine();
            switch (num){
                case 1:
                    bookFilter.setAuthorId(choseAuthor(userService, sc));
                    break;
                case 2:
                    bookFilter.setGenreId(choseGenre(genreService, sc));
                    break;
                case 3:
                    bookFilter.setBookSortStrategy(choseSortStrategy(sc));
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Вы ввели несуществующий номер пункта");
            }
            System.out.println("Сейчас выбрано:");
            if (bookFilter.getAuthorId() != null){
                User author = userService.findById(bookFilter.getAuthorId());
                System.out.println("Автор - " + author.getNickname());
            }
            if (bookFilter.getGenreId() != null){
                Genre genre = genreService.findById(bookFilter.getGenreId());
                System.out.println("Жанр - " + genre.getName());
            }
            if (bookFilter.getBookSortStrategy() != null){
                System.out.println("Сортировка - " + bookFilter.getBookSortStrategy().getDescription());
            }
            System.out.println("Продолжим");
        } while (num != 4);
        List<Book> books = bookService.findFullBooksByBookFilter(bookFilter);

        System.out.println("Результат");
        for (Book book : books){
            System.out.print(book.getId() + " " + book.getTitle() + " " + book.getCreatedAt().toLocalDate() + " ");
            if (book.getGenres() != null){
                for (Genre genre : book.getGenres()){
                    System.out.print("Жанры - ");
                    System.out.print(genre.getName() + " ");
                }
            }
            System.out.println();
        }
    }

    //вспомогательные
    private static Long choseAuthor(UserService userService, Scanner sc){
        System.out.println("Введите никнейм автора, книги которого хотите найти:");
        String authorNickname = sc.nextLine();
        User author = userService.findUserByNickname(authorNickname);
        if (author == null){
            System.out.println("Предупреждение: вы ввели никнейм несуществующего автора, фильтр не настроен");
        }
        return author.getId();
    }

    private static Long choseGenre(GenreService genreService, Scanner sc){
        findAllGenres(genreService);
        System.out.println("Выберите жанр, по которому хотите выполнить поиск(Введите id):");
        Long genreId = sc.nextLong();
        sc.nextLine();

        return genreId;
    }

    private static BookSortStrategy choseSortStrategy(Scanner sc){
        BookSortStrategy[] strategies = BookSortStrategy.values();
        for (BookSortStrategy strategy : strategies){
            System.out.println(strategy + " - " + strategy.getDescription());
        }
        System.out.println("NULL - без сортировки");
        System.out.println("Введите какой способ сортировки результатов вы хотите использовать(Название):");
        String strategy = sc.nextLine();
        try {
            return BookSortStrategy.valueOf(strategy.toUpperCase());
        }
        catch (IllegalArgumentException e){
            System.out.println("Предупреждение: выбрано без сортировки");
            return null;
        }
    }

    //11
    public static void openBook(BookService bookService, UserService userService,
                                GenreService genreService, VolumeService volumeService,
                                ChapterService chapterService, Scanner sc){
        System.out.println("Хотите ли вы сначала найти книги(Введите номер пункта)?");
        System.out.println("1. Посмотреть все мои книги");
        System.out.println("2. Поиск книг");
        System.out.println("3. Я уже знаю айди нужной мне книги");
        int num = sc.nextInt();
        sc.nextLine();
        switch (num){
            case 1:
                findAllMyBook(bookService);
                break;
            case 2:
                findBooks(bookService, userService, genreService, sc);
                break;
            case 3:
                break;
            default:
                System.out.println("Предупреждение: вы ввели неверный номер пункта. Поиск не был совершен");
        }
        System.out.println("Теперь вы можете открыть книгу");
        System.out.println("Введите айди книги, которую вы хотите открыть:");
        Long bookId = sc.nextLong();
        sc.nextLine();
        Book book = bookService.findById(bookId);
        if (book == null){
            throw new ServiceException("Вы ввели несуществующий айди");
        }
        Long volumeId;
        if(volumeService.existsNotDefaultVolume(bookId)){
            System.out.println("Тома");
            findAllVolumes(bookId, volumeService);
            System.out.println("Выберите, какой том хотите открыть(Введите айди):");
            volumeId = sc.nextLong();
            sc.nextLine();
            Volume volume = volumeService.findById(volumeId);
            if (volume == null){
                throw new ServiceException("Вы ввели несуществующий айди");
            }
        }
        else{
            volumeId = volumeService.findDefaultVolume(bookId).getId();
        }
        System.out.println("Главы");
        findAllChapters(volumeId, chapterService);
        System.out.println("Выберите, какую главу хотите открыть(Введите айди):");
        Long chapterId = sc.nextLong();
        sc.nextLine();
        Chapter chapter = chapterService.findFullChapter(chapterId);
        if (chapter == null){
            throw new ServiceException("Вы ввели несуществующий айди");
        }
        FractionalNumber chapterNumber = new FractionalNumber(chapter.getChapterMainNumber(), chapter.getChapterSubNumber());
        System.out.println("Глава");
        System.out.println(chapter.getTitle() + " " + mapToDouble(chapterNumber));
        System.out.println(chapter.getContent());
    }

    //12
    public static void deleteBook(BookService bookService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        findAllMyBook(bookService);
        System.out.println("Введите айди книги, которую хотите удалить:");

        Long bookId = sc.nextLong();

        sc.nextLine();
        Book book = bookService.findById(bookId);
        if (book == null){
            throw new ServiceException("Вы ввели несуществующий айди");
        }
        System.out.println("Вы точно хотите удалить книгу " + book.getTitle() + "?(да/нет)");
        String yesOrNo = sc.nextLine().toLowerCase();
        if (!(yesOrNo.equals("да") || yesOrNo.equals("yes"))){
            System.out.println("Книга не удалена");
            return;
        }

        bookService.deleteBook(bookId, currentUser.getId());
        System.out.println("Книга удалена");
    }

    //13
    public static void deleteVolume(VolumeService volumeService, BookService bookService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        findAllMyBook(bookService);
        System.out.println("Введите айди книги, том которой вы хотите удалить:");

        Long bookId = sc.nextLong();

        sc.nextLine();
        Book book = bookService.findById(bookId);
        if (book == null){
            throw new ServiceException("Вы ввели несуществующий айди");
        }

        findAllVolumes(bookId, volumeService);
        System.out.println("Введите айди тома, который вы хотите удалить:");
        Long volumeId = sc.nextLong();
        sc.nextLine();
        Volume volume = volumeService.findById(volumeId);
        if (volume == null){
            throw new ServiceException("Вы ввели несуществующий айди");
        }
        System.out.println("Вы точно хотите удалить том " + volume.getTitle() + "?(да/нет)");
        String yesOrNo = sc.nextLine().toLowerCase();
        if (!(yesOrNo.equals("да") || yesOrNo.equals("yes"))){
            System.out.println("Том не удален");
            return;
        }

        volumeService.deleteVolume(volumeId, currentUser.getId());
        System.out.println("Том удален");
    }

    //14
    public static void deleteChapter(ChapterService chapterService, VolumeService volumeService,
                                     BookService bookService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        findAllMyBook(bookService);
        System.out.println("Введите айди книги, главу которой вы хотите удалить:");

        Long bookId = sc.nextLong();

        sc.nextLine();
        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new ServiceException("Вы ввели несуществующий айди");
        }

        findAllVolumes(bookId, volumeService);
        System.out.println("Введите айди тома, главу которой вы хотите удалить:");
        Long volumeId = sc.nextLong();
        sc.nextLine();
        Volume volume = volumeService.findById(volumeId);
        if (volume == null) {
            throw new ServiceException("Вы ввели несуществующий айди");
        }

        findAllChapters(volumeId, chapterService);
        System.out.println("Введите айди главы, которую вы хотите удалить:");
        Long chapterId = sc.nextLong();
        sc.nextLine();
        Chapter chapter = chapterService.findInfoById(chapterId);
        System.out.println("Вы точно хотите удалить главу " + chapter.getTitle() + "?(да/нет)");
        String yesOrNo = sc.nextLine().toLowerCase();
        if (!(yesOrNo.equals("да") || yesOrNo.equals("yes"))) {
            System.out.println("Глава не удалена");
            return;
        }

        chapterService.deleteChapter(chapterId, currentUser.getId());
        System.out.println("Глава удалена");
    }

    //15
    public static void changeUserInfo(UserService userService, PasswordResetCodeService resetCodeService, Scanner sc){
        System.out.println("1. Сменить пароль с помощью старого пароля (нужно войти в аккаунт)");
        System.out.println("2. Сменить пароль с помощью почты и кода");
        System.out.println("3. Сменить никнейм (нужно войти в аккаунт)");
        System.out.println("Выберите пункт(Введите номер пункта):");
        int num = sc.nextInt();
        sc.nextLine();
        switch (num){
            case 1:
                changePasswordByLoginAndOldPassword(userService, sc);
                break;
            case 2:
                changePasswordByEmail(userService, resetCodeService, sc);
                break;
            case 3:
                changeNickname(userService, sc);
                break;
            default:
                System.out.println("Вы ввели неверный номер пункта");
                break;
        }
    }

    //вспомогательные
    private static void changePasswordByLoginAndOldPassword(UserService userService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        System.out.println("Введите старый пароль");
        String oldPassword = sc.nextLine();

        System.out.println("Введите новый пароль");
        String newPassword = sc.nextLine();

        userService.changePassword(currentUser.getId(), oldPassword, newPassword);
        System.out.println("Пароль изменен");
    }

    private static void changePasswordByEmail(UserService userService, PasswordResetCodeService resetCodeService, Scanner sc){
        System.out.println("Введите ваш email:");

        String email = sc.nextLine();
        System.out.println("Введите новый пароль:");
        String newPassword = sc.nextLine();
        resetCodeService.sendCode(email);

        System.out.println("Введите код, который был отправлен к вам на почту");
        String code = sc.nextLine();

        userService.changePassword(email, code, newPassword);
        System.out.println("Пароль изменен");
    }

    private static void changeNickname(UserService userService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        System.out.println("Введите новый никнейм:");
        String newNickname = sc.nextLine();
        userService.changeNickname(currentUser.getId(), newNickname);

        System.out.println("Никнейм изменен");
    }

    //16
    public static void logout(){
        SessionContext.logout();
        System.out.println("Вы вышли из аккаунта");
    }

    //17
    public static void deleteAccount(UserService userService, Scanner sc){
        if (!SessionContext.isLogged()){
            throw new AuthorizationException("Вы не вошли в аккаунт");
        }
        User currentUser = SessionContext.getUser();

        System.out.println("Вы хотите, чтобы вместе с вашим аккаунтом были удалены и ваши книги, где вы единственный автор?(да/нет/отмена)");
        System.out.println("После удаления аккаунта ваше авторство будет невозможно восстановить");

        String yesOrNoDeleteBooks = sc.nextLine().toLowerCase();
        boolean deleteBooks;
        if (yesOrNoDeleteBooks.equals("да") || yesOrNoDeleteBooks.equals("yes")){
            deleteBooks = true;
        }
        else if (yesOrNoDeleteBooks.equals("нет") || yesOrNoDeleteBooks.equals("no")){
            deleteBooks = false;
        }
        else{
            System.out.println("Удаление аккаунта отменено");
            return;
        }

        System.out.println("Вы точно хотите удалить аккаунт " + currentUser.getEmail() + "?(да/нет)");
        String yesOrNoDeleteAccount = sc.nextLine().toLowerCase();
        if (!(yesOrNoDeleteAccount.equals("да") || yesOrNoDeleteAccount.equals("yes"))){
            System.out.println("Удаление аккаунта отменено");
            return;
        }

        userService.deleteUser(currentUser.getId(), deleteBooks);
        SessionContext.logout();
        System.out.println("Аккаунт удален");
    }

    //для номеров глав и томов
    private static FractionalNumber mapToFractionalNumber(double number){
        BigDecimal bigDecimal = BigDecimal.valueOf(number);
        if (bigDecimal.scale()>1){
            throw new ServiceException("Максимальное количество цифр после запятой - 1");
        }
        int mainNumber = (int) number;
        int subNumber = (int) Math.round((number - mainNumber) * 10);
        return new FractionalNumber(mainNumber, subNumber);
    }

    private static double mapToDouble(FractionalNumber fractionalNumber){
        return fractionalNumber.mainNumber() + fractionalNumber.SubNumber()*0.1;
    }
}
