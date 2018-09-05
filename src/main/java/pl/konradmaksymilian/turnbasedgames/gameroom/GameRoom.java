package pl.konradmaksymilian.turnbasedgames.gameroom;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.core.exception.BuilderNullPropertyException;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

public final class GameRoom {
	
	private int id;
	private final Instant creationTime;
	private final List<User> users = new ArrayList<>();
	private final Set<Invitation> invitations = new HashSet<>();
	private boolean areObserversAllowed;
	private RoomPrivacy privacy;
	private ChatPolicy chatPolicy;
	private GameEngine gameEngine;
	
	private GameRoom(Instant creationTime, User host, Set<Invitation> invitations, boolean areObserversAllowed,
			RoomPrivacy privacy, ChatPolicy chatPolicy, GameEngine gameEngine) {
		this.creationTime = creationTime;
		users.add(host);
		this.invitations.addAll(invitations);
		this.areObserversAllowed = areObserversAllowed;
		this.privacy = privacy;
		this.chatPolicy = chatPolicy;
		this.gameEngine = gameEngine;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Instant getCreationTime() {
		return creationTime;
	}

	public RoomPrivacy getPrivacy() {
		return privacy;
	}

	public void setPrivacy(RoomPrivacy privacy) {
		this.privacy = privacy;
	}
	
	public ChatPolicy getChatPolicy() {
		return chatPolicy;
	}

	public void setChatPolicy(ChatPolicy chatPolicy) {
		this.chatPolicy = chatPolicy;
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}

	public void setGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}

	public List<User> getUsers() {
		return users;
	}
	
	public User getHost() {
		return users.get(0);
	}

	public Set<Invitation> getInvitations() {
		return invitations;
	}
	
	public boolean areObserversAllowed() {
		return areObserversAllowed;
	}
	
	public void setObserversAllowed(boolean areObserversAllowed) {
		this.areObserversAllowed = areObserversAllowed;
	}
	
	public int countUsers() {
		return users.size();
	}
	
	public boolean containsUser(int userId) {
		return users.stream()
				.anyMatch(user -> user.getId() == userId);
	}
	
	public void addUser(User user) {
		users.add(user);
	}
	
	public boolean removeUser(int userId) {
		return users.removeIf(user -> user.getId() == userId);
	}
	
	public boolean isUserInvited(int userId) {
		return invitations.stream()
				.anyMatch(invitation -> invitation.getInviteeId() == userId);
	}
	
	public void addInvitation(Invitation invitation) {
		invitations.add(invitation);
	}
	
	public boolean removeInvitation(int invitedId) {
		return invitations.removeIf(invitation -> invitation.getInviteeId() == invitedId);
	}
	
	public boolean isObserver(int userId) {
		return containsUser(userId) && !gameEngine.containsPlayer(userId); 
	}
	
	public static class Builder {
		
		private Instant creationTime;
		private User host;
		private Set<Invitation> invitations;
		private Boolean areObserversAllowed;
		private RoomPrivacy privacy;
		private ChatPolicy chatPolicy;
		private GameEngine gameEngine;
		
		public Builder creationTime(Instant creationTime) {
			this.creationTime = creationTime;
			return this;
		}
		
		public Builder host(User host) {
			this.host = host;
			return this;
		}
		
		public Builder invitations(Set<Invitation> invitations) {
			this.invitations = invitations;
			return this;
		}
		
		public Builder observersAllowed(boolean areObserversAllowed) {
			this.areObserversAllowed = areObserversAllowed;
			return this;
		}
		
		public Builder privacy(RoomPrivacy privacy) {
			this.privacy = privacy;
			return this;
		}
		
		public Builder chatPolicy(ChatPolicy chatPolicy) {
			this.chatPolicy = chatPolicy;
			return this;
		}
		
		public Builder gameEngine(GameEngine gameEngine) {
			this.gameEngine = gameEngine;
			return this;
		}
		
		public GameRoom build() {
			if (host == null || areObserversAllowed == null || privacy == null || chatPolicy == null 
					|| gameEngine == null) {
				throw new BuilderNullPropertyException(GameRoom.class);
			}
			
			if (invitations == null) {
				invitations = Collections.emptySet();
			}
			
			if (creationTime == null) {
				creationTime = Instant.now();
			}
	
			return new GameRoom(creationTime, host, invitations, areObserversAllowed, privacy, chatPolicy, gameEngine);
		}
	}
}
