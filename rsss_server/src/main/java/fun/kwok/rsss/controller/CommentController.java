package fun.kwok.rsss.controller;


import fun.kwok.rsss.bean.Comment;
import fun.kwok.rsss.bean.ResultInfo;
import fun.kwok.rsss.service.CommentService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})

public class CommentController {
    @Autowired
    CommentService commentService;

    @ResponseBody
    @GetMapping({"/getUnReadComment"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo getUnReadComment() {
        List<Comment> unReadComment = this.commentService.getUnReadComment();
        int tatal = unReadComment.size();
        if (tatal > 0)
            return new ResultInfo(true, "success", Integer.valueOf(tatal), unReadComment);
        return new ResultInfo(false, "没有未读留言", Integer.valueOf(tatal), unReadComment);
    }

    @ResponseBody
    @GetMapping({"/getReadComment"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo getReadComment() {
        List<Comment> readComment = this.commentService.getReadComment();
        int tatal = readComment.size();
        if (tatal > 0)
            return new ResultInfo(true, "success", Integer.valueOf(tatal), readComment);
        return new ResultInfo(false, "没有已读留言", Integer.valueOf(tatal), readComment);
    }

    @ResponseBody
    @PutMapping({"/readComment"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo readComment(@RequestParam("id") Long id) {
        int i = this.commentService.readComment(id);
        if (i > 0)
            return new ResultInfo(true, "success", null);
        return new ResultInfo(false, "操作失败", null);
    }

    @ResponseBody
    @DeleteMapping({"/delComment/{id}"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo delComment(@PathVariable("id") Long id) {
        int i = this.commentService.deleteComment(id);
        if (i > 0)
            return new ResultInfo(true, "success", null);
        return new ResultInfo(false, "操作失败", null);
    }

    @ResponseBody
    @PostMapping({"/addComment"})
    public ResultInfo getUserById(@RequestParam(value = "orderId", required = false) Long orderId, @RequestParam(value = "content", required = false) String content, @RequestParam("contact") String contact) {
        Comment comment = new Comment();
        comment.setOrderId(orderId);
        comment.setContact(contact);
        comment.setContent(content);
        comment.setStatus(0);
        comment.setDatatime(new Date());
        int i = commentService.addComment(comment);
        if (i > 0)
            return new ResultInfo(true, "提交成功", null);
        return new ResultInfo(false, "提交失败", null);
    }

    @ResponseBody
    @GetMapping({"/getUnReadCommentCount"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo getUnReadCommentCount() {
        int count = this.commentService.getUnReadCommentCount();
        return new ResultInfo(true, "", Integer.valueOf(count), null);
    }
}
