package app.service;

import app.model.Lesson;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * Lesson service.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
@Service
@Transactional
public class LessonService {
    public List<Lesson> getLessons() {
        return Collections.emptyList();
    }

    public Lesson getLesson(String lessonId) {
        return new Lesson();
    }
}
