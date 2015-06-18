package vivid.repository.cassandra;

import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import vivid.feed.Comment;
import vivid.feed.compositekey.CommentKey;

/**
 * Created by xiezhuohan on 15-6-12.
 */
public interface CommentRepository extends TypedIdCassandraRepository<Comment, CommentKey> {

}
