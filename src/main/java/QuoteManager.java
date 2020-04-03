import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import domain.Quote;
import exceptions.QuoteException;
import exceptions.QuoteIdExistsException;
import exceptions.QuoteIdNotExistsException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class QuoteManager {

    Dao<Quote, Integer> quoteDao;

    public QuoteManager() {
        this("quoteHandson.db");
    }

    public QuoteManager(String dbName) {
        try {
            var connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + dbName);
            TableUtils.createTableIfNotExists(connectionSource, Quote.class);
            this.quoteDao = DaoManager.createDao(connectionSource, Quote.class);
        } catch (SQLException e) {
            throw new QuoteException("Something related to connection happened!", e);
        }
    }

    /**
     * @param id
     * @param quote
     * @param from
     * @return
     */
    public Quote addQuote(int id, String quote, String from) {
        return addQuote(new Quote(id, quote, from));
    }

    /**
     * DON'T CHANGE THE SIGNATURE!
     *
     * @param quote
     * @return
     * @throws QuoteIdExistsException when the id of the new quote is already in the DB.
     * @throws QuoteException         for any other exceptions.
     */
    public Quote addQuote(Quote quote) {
        // TODO
        try {
            var newQuoteId = quote.getId();
            if (quoteDao.idExists(newQuoteId)) {
                throw new QuoteIdExistsException(newQuoteId);
            } else {
                var newQuote = quoteDao.createIfNotExists(quote);
                return newQuote;
            }
        } catch (SQLException e) {
            throw new QuoteException("Something happened on when creating the new quote", e);
        }
    }

    /**
     * DON'T CHANGE THE SIGNATURE!
     *
     * @return
     * @throws QuoteException for any other exceptions.
     */
    public List<Quote> getAllQuotes() {
        try {
            List<Quote> allQuotes = quoteDao.queryForAll();
            return  allQuotes;
        } catch(SQLException e) {
            throw new QuoteException("Something happened when trying to get all quotes", e);
        }
    }

    /**
     * Update 'quote' part of a quote of quote with 'id'
     *
     * @param id
     * @param quote
     * @return the updated quote
     * @throws QuoteIdNotExistsException when id doesn't exist
     * @throws QuoteException            for any other exceptions.
     */
    public Quote updateQuoteText(int id, String quote) {
        try {
            if (quoteDao.idExists(id)) {
                var theQuote = quoteDao.queryForId(id);
                quoteDao.update(new Quote(id, quote, theQuote.getFrom()));
                return quoteDao.queryForId(id);
            } else {
                throw new QuoteIdNotExistsException(id);
            }
        } catch (SQLException e) {
            throw new QuoteException("Something happened on id check and update!", e);
        }
    }

    /**
     * Update 'from' part of a quote of quote with 'id'
     * DON'T CHANGE THE SIGNATURE!
     *
     * @param id
     * @param from
     * @return the updated quote
     * @throws QuoteIdNotExistsException when id doesn't exist
     * @throws QuoteException            for any other exceptions.
     */
    public Quote updateQuoteFrom(int id, String from) {
        try {
            if (quoteDao.idExists(id)) {
                var theQuote = quoteDao.queryForId(id);
                quoteDao.update(new Quote(id, theQuote.getQuote(), from));
                return quoteDao.queryForId(id);
            } else {
                throw new QuoteIdNotExistsException(id);
            }
        } catch (SQLException e) {
            throw new QuoteException("Something happened when attempting to update!", e);
        }
    }

    /**
     * DON'T CHANGE THE SIGNATURE!
     *
     * @param id
     * @return a copy of the delete quote
     * @throws QuoteIdNotExistsException when id doesn't exist
     * @throws QuoteException            for any other exceptions.
     */
    public Quote deleteQuote(int id) {
        // TODO
        try {
            if (quoteDao.idExists(id)) {
                var deletedQuote = quoteDao.queryForId(id);
                var deleteQuote = quoteDao.deleteById(id);
                return deletedQuote;
            } else {
                throw new QuoteIdNotExistsException(id);
            }
        } catch (SQLException e) {
            throw new QuoteException("Something happened on when deleting the quote", e);
        }
    }

    public void disposeResources() {
        try {
            quoteDao.getConnectionSource().close();
        } catch (IOException e) {
            throw new QuoteException("Couldn't close the source!", e);
        }
    }
}
