package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.dto.store.NewsDTO;
import ru.eriknas.brokenstore.exception.NotFoundException;
import ru.eriknas.brokenstore.mappers.NewsMapper;
import ru.eriknas.brokenstore.entity.NewsEntity;
import ru.eriknas.brokenstore.repository.NewsRepository;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    private static final String NEWS_NOT_FOUND = "Новость с id=%s не найдена";

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public NewsEntity addNews(NewsDTO newsDTO) {
        return newsRepository.save(NewsMapper.toEntity(newsDTO));
    }

    public NewsEntity getNewsById(int newsId) {

        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException(String.format(NEWS_NOT_FOUND, newsId)));
    }

    public Page<NewsEntity> getAllNews(int page, int size) {
        return newsRepository.findAll(PageRequest.of(page, size));
    }

    public void deleteById(int newsId) {
        newsRepository.deleteById(newsId);
    }
}