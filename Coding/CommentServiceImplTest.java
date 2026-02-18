package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import com.chuwa.redbook.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.junit.juiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtebdWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setup() {
        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
    }

    // findById
    @Test
    void testFindByIdSuccess() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment result = commentService.findById(1L);

        assertNotNull(result);
        assertEquals("Test comment", result.getContent());
        verify(commentRepository, times(1)).findById(1L);
    }

    // findById -not found branch
    @Test
    void testFindByIdNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.findById(1L);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    // save - success
    @Test
    void testSaveComment() {
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.save(comment);

        assertNotNull(result);
        verify(commentRepository).save(comment);
    }

    // update - success
    @Test
    void testUpdateSuccess() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        comment.setContent("Updated");

        Comment result = commentService.update(1L, comment);

        assertEquals("Updated", result.getContent());
        verify(commentRepository).save(any(Comment.class));
    }

    // update - not found branch
    @Test
    void testUpdateNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            commentService.update(1L, comment);
        });
    }

    // delete - success
    @Test
    void testDeleteSuccess() {
        when(commentRepository.existsById(1L)).thenReturn(true);

        commentService.delete(1L);

        verify(commentRepository).deleteById(1L);
    }

    // delete - not found
    @Test
    void testDeleteNotFound() {
        when(commentRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            commentService.delete(1L);
        });
    }

    // findAll
    @Test
    void testFindAll() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentRepository.findAll()).thenReturn(comments);

        List<Comment> result = commentService.findAll();

        assertEquals(1, result.size());
        verify(commentRepository).findAll();
    }
}