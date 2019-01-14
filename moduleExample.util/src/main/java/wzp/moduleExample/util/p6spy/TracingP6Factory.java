package wzp.moduleExample.util.p6spy;

import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.P6SpyFactory;

public class TracingP6Factory extends P6SpyFactory {
	@Override
	public JdbcEventListener getJdbcEventListener() {
		return new TracingJdbcEventListener();
	}
}