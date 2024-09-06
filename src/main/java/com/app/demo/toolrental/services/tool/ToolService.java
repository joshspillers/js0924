package com.app.demo.toolrental.services.tool;

import com.app.demo.toolrental.datamodel.aggregate.tool.ToolEntity;
import com.app.demo.toolrental.datamodel.aggregate.tool.ToolRepository;
import com.app.demo.toolrental.transport.ToolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ToolService {
    private final ToolRepository toolRepository;

    @Autowired
    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public Optional<ToolDTO> getToolById(long id) {
        if (id == 0) {
            return Optional.empty();
        }

        Optional<ToolEntity> toolEntity = toolRepository.findById(id);
        return toolEntity.map(ToolDTO::convertToDTO);
    }

    public Optional<ToolDTO> getToolByCode(String code) {
        if (code == null) {
            return Optional.empty();
        }

        Optional<ToolEntity> toolEntity = toolRepository.findByCodeIgnoreCase(code);
        return toolEntity.map((ToolDTO::convertToDTO));
    }

    public boolean isToolCodeExist(String code) {
        if (code == null) {
            return false;
        }

        Optional<ToolEntity> toolEntity = toolRepository.findByCodeIgnoreCase(code);
        return toolEntity.isPresent();
    }
}
