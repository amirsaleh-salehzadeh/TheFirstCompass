package common.events;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the events_attendance database table.
 * 
 */
@Entity
@Table(name="events_attendance")
public class EventsAttendance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="username")
	private String userName;

	//bi-directional many-to-one association to Event
	@ManyToOne
	@JoinColumn(name="events_id")
	private EventENT event;

	public EventsAttendance() {
	}

	public EventENT getEventENT() {
		return this.event;
	}

	public void setEventENT(EventENT event) {
		this.event = event;
	}

}