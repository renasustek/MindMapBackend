package com.github.renas.service;

import com.github.renas.persistance.LabelRepo;
import com.github.renas.persistance.models.LabelDao;
import com.github.renas.requests.task.Label;
import com.github.renas.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LabelService {

    private final LabelRepo labelRepo;

    public LabelService(LabelRepo labelRepo) {
        this.labelRepo = labelRepo;
    }

    // CREATE - Save a new label
    public Label createLabel(Label label) {
        LabelDao labelDao = new LabelDao();
        labelDao.setId(UUID.randomUUID()); // Generate UUID
        labelDao.setName(label.name());

        LabelDao savedLabel = labelRepo.save(labelDao);
        return new Label(savedLabel.getId(), savedLabel.getName());
    }

    // READ - Get all labels
    public List<Label> getAllLabels() {
        return labelRepo.findAll().stream()
                .map(labelDao -> new Label(labelDao.getId(), labelDao.getName()))
                .collect(Collectors.toList());
    }

    // READ - Get a label by ID
    public Label getLabelById(UUID id) {
        LabelDao labelDao = labelRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + id));
        return new Label(labelDao.getId(), labelDao.getName());
    }

    // UPDATE - Update an existing label
    public Label updateLabel(UUID id, Label label) {
        LabelDao labelDao = labelRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + id));

        labelDao.setName(label.name());
        LabelDao updatedLabel = labelRepo.save(labelDao);

        return new Label(updatedLabel.getId(), updatedLabel.getName());
    }

    // DELETE - Delete a label
    public void deleteLabel(UUID id) {
        if (!labelRepo.existsById(id)) {
            throw new ResourceNotFoundException("Label not found with ID: " + id);
        }
        labelRepo.deleteById(id);
    }
}
