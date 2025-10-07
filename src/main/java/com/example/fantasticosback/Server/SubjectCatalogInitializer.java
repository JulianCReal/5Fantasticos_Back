package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Repository.SubjectRepository;
import com.example.fantasticosback.util.SubjectCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SubjectCatalogInitializer implements CommandLineRunner {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeCatalogInDatabase();
    }

    /**
     * Inicializa el catálogo de materias en la base de datos si no existe
     */
    public void initializeCatalogInDatabase() {
        Map<String, Subject> catalogSubjects = SubjectCatalog.getSubjects();

        for (Map.Entry<String, Subject> entry : catalogSubjects.entrySet()) {
            String code = entry.getKey();
            Subject catalogSubject = entry.getValue();

            // Verificar si la materia ya existe en la BD
            Subject existingSubject = subjectRepository.findById(catalogSubject.getSubjectId()).orElse(null);

            if (existingSubject == null) {
                // Si no existe, guardarla en la BD
                subjectRepository.save(catalogSubject);
                System.out.println("Materia inicializada en BD: " + code + " - " + catalogSubject.getName());
            }
        }
    }
}
