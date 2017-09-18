package it.retriever.test;

import org.junit.Test;

import it.db.retriever.core.ConnectionProvider;
import it.db.retriever.core.configuration.entity.DataSource;

public class ConnectionTest {

	@Test
	public void connectionTest() {
		
		DataSource ds = new DataSource("oracle_ds","jdbc:oracle:thin:@(DESCRIPTION_LIST=(LOAD_BALANCE=off)(FAILOVER=on)(DESCRIPTION=(CONNECT_TIMEOUT=3)(RETRY_COUNT=3)(ADDRESS_LIST=(LOAD_BALANCE=on)(FAILOVER=ON)(ADDRESS=(PROTOCOL=tcp)(HOST=sc01-prodbccsi-scan.gbi.bcc.it)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=prim_PO11GSIN)))(DESCRIPTION=(CONNECT_TIMEOUT=3)(RETRY_COUNT=3)(ADDRESS_LIST=(LOAD_BALANCE=on)(FAILOVER=ON)(ADDRESS=(PROTOCOL=tcp)(HOST=sc02-prodbccsi-scan.gbi.bcc.it)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=prim_PO11GSIN))))","wsuser","fd6hdx34","oracle.jdbc.driver.OracleDriver");
		assert(ConnectionProvider.testConnection(ds));
	}
}
