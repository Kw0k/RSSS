package fun.kwok.rsss.service;
import fun.kwok.rsss.bean.Comment;
import fun.kwok.rsss.mapper.CommentMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    public int getUnReadCommentCount() {
        return this.commentMapper.getUnReadCommentCount();
    }

    public int getCommentCountOfToday() {
        return this.commentMapper.getCommentCountOfToday();
    }

    public List<Comment> getUnReadComment() {
        return this.commentMapper.getUnReadComment();
    }

    public List<Comment> getReadComment() {
        return this.commentMapper.getReadComment();
    }

    public int readComment(Long id) {
        return this.commentMapper.readComment(id);
    }

    public int deleteComment(Long id) {
        return this.commentMapper.deleteComment(id);
    }

    public int addComment(Comment comment) {
        return this.commentMapper.addComment(comment);
    }
}
