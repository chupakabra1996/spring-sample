package ru.kpfu.itis.security.cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.model.entity.RememberMeToken;
import ru.kpfu.itis.repository.LoginTokensRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Date;

/**
 * PersistentTokenRepository custom implementation for remember-me functionality
 */

@Repository("hibernatePersistentTokenRepository")
@Transactional
public class HibernateTokenRepositoryImpl implements PersistentTokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(HibernateTokenRepositoryImpl.class);

    @Autowired
    private LoginTokensRepository loginTokensRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {

        logger.error("[Creating new token for user : `" + token.getUsername() + "`]");

        RememberMeToken rememberMeToken = new RememberMeToken();

        rememberMeToken.setUsername(token.getUsername());
        rememberMeToken.setSeries(token.getSeries());
        rememberMeToken.setToken(token.getTokenValue());
        rememberMeToken.setLast_used(token.getDate());

        loginTokensRepository.save(rememberMeToken);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {

        logger.error("[Get token for series : `" + series + "`]");

        RememberMeToken rememberMeToken = loginTokensRepository.findBySeries(series);

        if (rememberMeToken == null) return null;

        return new PersistentRememberMeToken(rememberMeToken.getUsername(),
                rememberMeToken.getSeries(), rememberMeToken.getToken(), rememberMeToken.getLast_used());
    }

    @Override
    public void removeUserTokens(String username) {

        logger.error("[Removing token if any for user : `" + username + "`]");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaDelete<RememberMeToken> delete = cb.createCriteriaDelete(RememberMeToken.class);

        Root root = delete.from(RememberMeToken.class);

        delete
                .where(cb.equal(root.get("username"), username));

        entityManager.createQuery(delete).executeUpdate();

    }

    @Override
    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {

        logger.error("[Update token : `" + tokenValue + "`]");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaUpdate<RememberMeToken> update = cb.createCriteriaUpdate(RememberMeToken.class);

        Root root = update.from(RememberMeToken.class);

        update
                .set("token", tokenValue)
                .set("last_used", lastUsed)
                .where(cb.equal(root.get("series"), seriesId));

        entityManager.createQuery(update).executeUpdate();
    }

}