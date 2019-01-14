package wzp.moduleExample.util.p6spy;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.SimpleJdbcEventListener;

public class TracingJdbcEventListener extends SimpleJdbcEventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(TracingJdbcEventListener.class);
	
	@Override
	public void onAfterAnyExecute(StatementInformation info, long elapsed, SQLException e) {
		String sql = info.getSqlWithValues();
	}
}
