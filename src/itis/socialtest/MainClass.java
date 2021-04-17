package itis.socialtest;


import itis.socialtest.entities.Author;
import itis.socialtest.entities.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * В папке resources находятся два .csv файла.
 * Один содержит данные о постах в соцсети в следующем формате: Id автора, число лайков, дата, текст
 * Второй содержит данные о пользователях  - id, никнейм и дату рождения
 *
 * Напишите код, который превратит содержимое файлов в обьекты в package "entities"
 * и осуществите над ними следующие опреации:
 *
 * 1. Выведите в консоль все посты в читабельном виде, с информацией об авторе.
 * 2. Выведите все посты за сегодняшнюю дату
 * 3. Выведите все посты автора с ником "varlamov"
 * 4. Проверьте, содержит ли текст хотя бы одного поста слово "Россия"
 * 5. Выведите никнейм самого популярного автора (определять по сумме лайков на всех постах)
 *
 * Для выполнения заданий 2-5 используйте методы класса AnalyticsServiceImpl (которые вам нужно реализовать).
 *
 * Требования к реализации: все методы в AnalyticsService должны быть реализованы с использованием StreamApi.
 * Использование обычных циклов и дополнительных переменных приведет к снижению баллов, но допустимо.
 * Парсинг файлов и реализация методов оцениваются ОТДЕЛЬНО
 *
 *
 * */

public class MainClass {

    private List<Post> allPosts;

    private AnalyticsService analyticsService = new AnalyticsServiceImpl();

    public static void main(String[] args) throws IOException {
        new MainClass().run("src/itis/socialtest/resources/PostDatabase.csv",
                "src/itis/socialtest/resources/Authors.csv");
    }

    private void run(String postsSourcePath, String authorsSourcePath) throws IOException {

        FileReader postsFileReader = new FileReader(postsSourcePath);
        BufferedReader bufferedPostsFileReader = new BufferedReader(postsFileReader);

        FileReader authorsFileReader = new FileReader(authorsSourcePath);
        BufferedReader bufferedAuthorsFileReader = new BufferedReader(authorsFileReader);


        List<Author> authorList = new ArrayList<>();
        String[] authorData = bufferedAuthorsFileReader.readLine().split(", ");

        while (authorData.length > 1) {
            authorList.add(
                    new Author(
                            Long.parseLong(authorData[0]),
                            authorData[1],
                            authorData[2]
                    )
            );
            String line = bufferedAuthorsFileReader.readLine();
            if (Objects.isNull(line)){
                break;
            }
            authorData = line.split(", ");
        }


        List<Post> postList = new ArrayList<>();
        String[] postData = bufferedPostsFileReader.readLine().split(", ");

        while (postData.length > 1){
            Long authorId = Long.parseLong(postData[0]);
            Author author = authorList.stream()
                    .filter(author_ -> author_.getId().equals(authorId))
                    .findFirst().orElseThrow(() -> new RuntimeException("Author does not exist"));

            postList.add(
                    new Post(postData[2],
                            postData[3],
                            Long.parseLong(postData[1]),
                            author
                    )
            );
            String line = bufferedPostsFileReader.readLine();
            if (Objects.isNull(line)){
                break;
            }
            postData = line.split(", ");
        }

        postList.stream().forEach(post -> printPost(post));

        AnalyticsService analyticsService = new AnalyticsServiceImpl();

        // Task 2
        System.out.println("Task 2 output: ");
        analyticsService.findPostsByDate(postList, "17.04.2021T10:00").stream()
                .forEach(post -> printPost(post));

        // Task 3
        System.out.println("Task 3 output: ");
        analyticsService.findAllPostsByAuthorNickname(postList, "varlamov").stream()
                .forEach(post -> printPost(post));

        // Task 4
        System.out.println("Task 4 output: ");
        System.out.println(analyticsService.checkPostsThatContainsSearchString(postList, "Россия"));

        // Task 5
        System.out.println("Task 5 output: ");
        System.out.println(analyticsService.findMostPopularAuthorNickname(postList));
    }

    private void printPost(Post post){
        System.out.println(
                "Author: \n"
                        + "\t Nickname: "+ post.getAuthor().getNickname() + "\n"
                        + "\t Id: "+ post.getAuthor().getId() + "\n"
                        + "\t Birth date: "+ post.getAuthor().getBirthdayDate() + "\n" +
                "Date: " + post.getDate() + "\n" +
                "Likes count: " + post.getLikesCount() + "\n" +
                "Content: " + post.getContent() + "\n"
        );
    }
}
