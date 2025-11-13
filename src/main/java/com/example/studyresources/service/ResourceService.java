package com.example.studyresources.service;

import com.example.studyresources.model.Resource;
import com.example.studyresources.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Resource saveResource(MultipartFile file, String name, String description) {
        String fileName = fileStorageService.storeFile(file);

        Resource resource = new Resource();
        resource.setName(name);
        resource.setDescription(description);
        resource.setFileName(fileName);
        resource.setContentType(file.getContentType());
        resource.setSize(file.getSize());
        resource.setUploadDate(new Date());
        resource.setRating(0);
        resource.setRatingCount(0);

        return resourceRepository.save(resource);
    }

    public Resource getResource(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Resource rateResource(Long id, int rating) {
        Resource resource = getResource(id);
        if (resource != null) {
            double currentRating = resource.getRating();
            int ratingCount = resource.getRatingCount();
            double newRating = (currentRating * ratingCount + rating) / (ratingCount + 1);
            resource.setRating(newRating);
            resource.setRatingCount(ratingCount + 1);
            return resourceRepository.save(resource);
        }
        return null;
    }
}
