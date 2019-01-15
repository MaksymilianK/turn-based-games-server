package pl.konradmaksymilian.turnbasedgames.room.data.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.core.exception.BuilderNullPropertyException;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.room.business.RoomMessageSender;
import pl.konradmaksymilian.turnbasedgames.room.data.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.room.data.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.user.data.entity.User;

public final class Room {
	
	private int id = -1;
	private final Instant creationTime;
	private User host;
	private final List<User> users = new ArrayList<>();
	private final Set<Invitation> invitations = new HashSet<>();
	private RoomSettings settings;
	private RoomMessageSender messageSender;
	private GameEngine gameEngine;
	
	private Room(Instant creationTime, User host, Set<Invitation> invitations, RoomSettings settings, 
			GameEngine gameEngine) {
		this.creationTime = creationTime;
		this.host = host;
		this.invitations.addAll(invitations);
		this.settings = settings;
		this.gameEngine = gameEngine;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		if (this.id == -1) {
			this.id = id;
		} else {
			throw new RoomException("The room's ID has been already set");
		}
	}
	
	public Instant getCreationTime() {
		return creationTime;
	}

	public RoomSettings getSettings() {
		return settings;
	}
	
	public RoomMessageSender getMessageSender() {
		if (messageSender == null) {
			throw new RoomException("A message sender has not been set");
		} else {
			return messageSender;
		}
	}
	
	public void injectMessageSender(RoomMessageSender messageSender) {
		if (messageSender == null) {
			this.messageSender = messageSender;
		} else {
			throw new RoomException("The message sender has been already set");
		}
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}

	public void setGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}

	public List<User> getUsers() {
		return Collections.unmodifiableList(users);
	}
	
	public User getHost() {
		return host;
	}
	
	public void setHost(User host) {
		this.host = host;
	}

	public Set<Invitation> getInvitations() {
		return Collections.unmodifiableSet(invitations);
	}
	
	public void addUser(User user) {
		users.add(user);
	}
	
	public boolean removeUser(int userId) {
		return users.removeIf(user -> user.getId() == userId);
	}
	
	public void addInvitation(Invitation invitation) {
		invitations.add(invitation);
	}
	
	public boolean removeInvitation(int invitedId) {
		return invitations.removeIf(invitation -> invitation.getInviteeId() == invitedId);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private Instant creationTime;
		private User host;
		private Set<Invitation> invitations;
		private Boolean areObserversAllowed;
		private RoomPrivacy privacy;
		private ChatPolicy chatPolicy;
		private GameEngine gameEngine;
		
		Builder() {}
		
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
		
		public Room build() {
			if (host == null || areObserversAllowed == null || privacy == null || chatPolicy == null 
					|| gameEngine == null) {
				throw new BuilderNullPropertyException(Room.class);
			}
			
			if (invitations == null) {
				invitations = Collections.emptySet();
			}
			
			if (creationTime == null) {
				creationTime = Instant.now();
			}
	
			return new Room(creationTime, host, invitations,
					new RoomSettings(areObserversAllowed, privacy, chatPolicy), gameEngine);
		}
	}
}
