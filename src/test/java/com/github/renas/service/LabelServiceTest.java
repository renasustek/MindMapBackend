package com.github.renas.service;

import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.persistance.LabelRepo;
import com.github.renas.persistance.models.LabelDao;
import com.github.renas.requests.LabelRequest;
import com.github.renas.requests.task.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class LabelServiceTest {

    @Mock
    private LabelRepo labelRepo;

    @InjectMocks
    private LabelService labelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLabel_returnsCreatedLabel() {
        LabelRequest request = new LabelRequest("Important");
        LabelDao savedDao = new LabelDao();
        savedDao.setUuid(UUID.randomUUID());
        savedDao.setName("Important");

        given(labelRepo.save(any(LabelDao.class))).willReturn(savedDao);

        Label result = labelService.createLabel(request);

        assertNotNull(result);
        assertEquals("Important", result.name());
    }

    @Test
    void getAllLabels_returnsListOfLabels() {
        LabelDao dao1 = new LabelDao();
        dao1.setUuid(UUID.randomUUID());
        dao1.setName("Urgent");

        LabelDao dao2 = new LabelDao();
        dao2.setUuid(UUID.randomUUID());
        dao2.setName("Optional");

        given(labelRepo.findAllForCurrentUser()).willReturn(List.of(dao1, dao2));

        List<Label> result = labelService.getAllLabels();

        assertEquals(2, result.size());
    }

    @Test
    void getLabelById_validId_returnsLabel() {
        UUID labelId = UUID.randomUUID();
        LabelDao dao = new LabelDao();
        dao.setUuid(labelId);
        dao.setName("Focus");

        given(labelRepo.findByIdUserId(labelId)).willReturn(Optional.of(dao));

        Label result = labelService.getLabelById(labelId);

        assertNotNull(result);
        assertEquals("Focus", result.name());
    }

    @Test
    void getLabelById_invalidId_throwsException() {
        UUID labelId = UUID.randomUUID();
        given(labelRepo.findByIdUserId(labelId)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> labelService.getLabelById(labelId));
    }

    @Test
    void updateLabel_validId_updatesAndReturnsLabel() {
        UUID labelId = UUID.randomUUID();
        LabelDao existingDao = new LabelDao();
        existingDao.setUuid(labelId);
        existingDao.setName("Old");

        LabelDao updatedDao = new LabelDao();
        updatedDao.setUuid(labelId);
        updatedDao.setName("New");

        given(labelRepo.findByIdUserId(labelId)).willReturn(Optional.of(existingDao));
        given(labelRepo.save(any(LabelDao.class))).willReturn(updatedDao);

        Label labelToUpdate = new Label(labelId, "New");
        Label result = labelService.updateLabel(labelId, labelToUpdate);

        assertEquals("New", result.name());
    }

    @Test
    void deleteLabel_validId_deletesSuccessfully() {
        UUID labelId = UUID.randomUUID();
        given(labelRepo.existsById(labelId)).willReturn(true);

        labelService.deleteLabel(labelId);

        verify(labelRepo, times(1)).deleteById(labelId);
    }

    @Test
    void deleteLabel_invalidId_throwsException() {
        UUID labelId = UUID.randomUUID();
        given(labelRepo.existsById(labelId)).willReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> labelService.deleteLabel(labelId));
    }
}
