package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.dto.store.NewsDTO;
import ru.eriknas.brokenstore.mappers.NewsMapper;
import ru.eriknas.brokenstore.models.entities.NewsEntity;
import ru.eriknas.brokenstore.repository.NewsRepository;

import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public NewsEntity addNews(NewsDTO newsDTO) {
        return newsRepository.save(NewsMapper.toEntity(newsDTO));
    }

    public Optional<NewsEntity> getNewsById(int id) {
        return newsRepository.findById(id);
    }

    public Page<NewsEntity> getAllNews(int page, int size) {
        return newsRepository.findAll(PageRequest.of(page, size));
    }

    public void deleteById(int id) {
        newsRepository.deleteById(id);
    }
}
