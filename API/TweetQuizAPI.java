import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import twitter4j.IDs;
import twitter4j.Logger;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

@Api(name = "tweetquizendpoint", version="v1")
public class TweetQuizAPI {
	
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	@ApiMethod(name = "play")
	public String play(@Named("playerName") String playerName){

		return playerName;
		
		
	}
	
	/*
	 * Connexion à un compte Twitter
	 */
	public Twitter connexion() {
		
    	Twitter twitter = new TwitterFactory().getInstance();

    	twitter.setOAuthConsumer("DGVfkSkL9v15unjXssXaAlkAU", "qkiM1kCTkX3gCXT8r3HpQCOE3dSPGLaSTXhnRoa3yhNKgiF8Cu");
		AccessToken myAccessToken = new AccessToken("258368311-qvQLVEtFy3GwAukTZrCme53gZbddu5hBrXfIfNYf","z5f6bsiE3YCVjVPUIJqQK2QL4o9NUc1ZbHUFVcWVgEfjJ");
		twitter.setOAuthAccessToken(myAccessToken);
		
		return twitter;
		
	}
	
	public void getTweetsFromTweeter(String playerName){
		try{
			//Connexion sur le compte Twitter de base
			Twitter twitter = connexion();
			
			long cursor = -1;
			IDs ids;
			
			 do {
				 //Récupération des ID des comptes suivis par le joueur
				 ids = twitter.getFriendsIDs(playerName, cursor);
				 
				
				 //Parcours des comptes suivis
				for (long id : ids.getIDs()) {
					
						User user = twitter.showUser(id);
						
						Paging paging = new Paging();
						paging.setCount(5);
						
						List<Status> lesStatuts = twitter.getUserTimeline(user.getScreenName(),paging);						
						//Boucle sur les statuts
						for (Status status : lesStatuts) {
							
							//Stockage du tweet
							Entity tweet = new Entity(user.getScreenName());
							tweet.setProperty("Autor", user.getName());
							tweet.setProperty("Content", status.getText());
							tweet.setProperty("IsAlreadyPlayed", false); //La question a déjà été posée
							tweet.setProperty("IsActive", true); 		 //Le tweet fait partie du jeu et n'a pas été archivé
							
							datastore.put(tweet);
		
						}	
						
				}
			} while ((cursor = ids.getNextCursor()) != 0);
			
		}catch (TwitterException ex) {
		}
	}
	
}
