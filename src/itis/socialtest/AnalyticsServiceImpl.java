package itis.socialtest;

import itis.socialtest.entities.Post;

import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsServiceImpl implements AnalyticsService {
    @Override
    public List<Post> findPostsByDate(List<Post> posts, String date) {
        return posts.stream()
                .filter(post -> post.getDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public String findMostPopularAuthorNickname(List<Post> posts) {
        return posts.stream()
                .collect(Collectors.groupingBy(
                        post -> post.getAuthor().getNickname(),
                        Collectors.counting()))
                .entrySet()
                .stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get().getKey();
    }

    @Override
    public Boolean checkPostsThatContainsSearchString(List<Post> posts, String searchString) {
        return posts.stream().anyMatch(post -> post.getContent().contains(searchString));
    }

    @Override
    public List<Post> findAllPostsByAuthorNickname(List<Post> posts, String nick){
        return posts.stream()
                .filter(post ->
                        post.getAuthor().getNickname().equals(nick))
                .collect(Collectors.toList());
    }
}
