package xyz.joestr.bungeeq.bungeecord_plugin.configuration;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.logging.Logger;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.LogEntry;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockEntry;

/**
 * @author joestr
 */
public class DatabaseConfiguration {

  private static DatabaseConfiguration instance;
  private static final Logger LOG = Logger.getLogger(DatabaseConfiguration.class.getSimpleName());

  private ConnectionSource connectionSource = null;
  private Dao<LogEntry, String> logEntryDao = null;
  private Dao<UnlockEntry, String> unlockEntryDao = null;

  private DatabaseConfiguration(String jdbcUri) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");

    this.connectionSource = new JdbcConnectionSource(jdbcUri);

    this.registerDaos(connectionSource);
    this.checkTables(connectionSource);
  }

  private void registerDaos(ConnectionSource connectionSource) throws SQLException {
    this.logEntryDao = DaoManager.createDao(connectionSource, LogEntry.class);
    this.unlockEntryDao = DaoManager.createDao(connectionSource, UnlockEntry.class);
  }

  private void checkTables(ConnectionSource connectionSource) throws SQLException {
    TableUtils.createTableIfNotExists(connectionSource, LogEntry.class);
    TableUtils.createTableIfNotExists(connectionSource, UnlockEntry.class);
  }

  public static DatabaseConfiguration getInstance(String jdbcUri)
    throws ClassNotFoundException, SQLException {
    if (instance != null) {
      throw new RuntimeException("This class has already been instantiated.");
    }

    instance = new DatabaseConfiguration(jdbcUri);

    return instance;
  }

  public static DatabaseConfiguration getInstance() {
    if (instance == null) {
      throw new RuntimeException("This class has not been initialized yet.");
    }

    return instance;
  }

  public ConnectionSource getConnectionSource() {
    return this.connectionSource;
  }

  public Dao<LogEntry, String> getLogEntryDao() {
    return logEntryDao;
  }

  public Dao<UnlockEntry, String> getUnlockEntryDao() {
    return unlockEntryDao;
  }
  
  
}
