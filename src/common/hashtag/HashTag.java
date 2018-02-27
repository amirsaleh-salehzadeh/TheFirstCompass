package common.hashtag;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the hash_tag database table.
 * 
 */
@Entity
@Table(name="hash_tag")
public class HashTag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="hash_tag_id")
	private String hashTagId;

	@Column(name="hash_tag")
	private String hashTag;

	//bi-directional many-to-one association to EventsHashTag
	@OneToMany(mappedBy="hashTag")
	private List<EventsHashTag> eventsHashTags;

	public HashTag() {
	}

	public String getHashTagId() {
		return this.hashTagId;
	}

	public void setHashTagId(String hashTagId) {
		this.hashTagId = hashTagId;
	}

	public String getHashTag() {
		return this.hashTag;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public List<EventsHashTag> getEventsHashTags() {
		return this.eventsHashTags;
	}

	public void setEventsHashTags(List<EventsHashTag> eventsHashTags) {
		this.eventsHashTags = eventsHashTags;
	}

	public EventsHashTag addEventsHashTag(EventsHashTag eventsHashTag) {
		getEventsHashTags().add(eventsHashTag);
		eventsHashTag.setHashTag(this);

		return eventsHashTag;
	}

	public EventsHashTag removeEventsHashTag(EventsHashTag eventsHashTag) {
		getEventsHashTags().remove(eventsHashTag);
		eventsHashTag.setHashTag(null);

		return eventsHashTag;
	}

}