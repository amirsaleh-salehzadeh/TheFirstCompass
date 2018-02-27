package common.hashtag;

import java.io.Serializable;
import javax.persistence.*;

import common.events.EventENT;


/**
 * The persistent class for the events_hash_tag database table.
 * 
 */
@Entity
@Table(name="events_hash_tag")
public class EventsHashTag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="event_hash_tag_id")
	private String eventHashTagId;

	//bi-directional many-to-one association to Event
	@ManyToOne
	@JoinColumn(name="event_id")
	private EventENT event;

	//bi-directional many-to-one association to HashTag
	@ManyToOne
	@JoinColumn(name="hash_tag_id")
	private HashTag hashTag;

	public EventsHashTag() {
	}

	public String getEventHashTagId() {
		return this.eventHashTagId;
	}

	public void setEventHashTagId(String eventHashTagId) {
		this.eventHashTagId = eventHashTagId;
	}

	public EventENT getEventENT() {
		return this.event;
	}

	public void setEventENT(EventENT event) {
		this.event = event;
	}

	public HashTag getHashTag() {
		return this.hashTag;
	}

	public void setHashTag(HashTag hashTag) {
		this.hashTag = hashTag;
	}

}