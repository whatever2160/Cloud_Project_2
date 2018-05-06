package main;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

import com.fasterxml.jackson.annotation.JsonProperty;


@TypeDiscriminator("doc.type === 'EligibleCouple'")

public class Tweet extends CouchDbDocument {

    @JsonProperty
    private String content;
    @JsonProperty
    private String timestamp;
    @JsonProperty
    private String coordinate_x;
    @JsonProperty
    private String coordinate_y;
    @JsonProperty
    private String score;
    @JsonProperty
    private String magnitude;
	@JsonProperty
	private String language;
	@JsonProperty
	private String sa2_name11;
	@JsonProperty
	private String mb_code11;

    public Tweet()
    {

    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getCoordinate_x() {
		return coordinate_x;
	}

	public void setCoordinate_x(String coordinate_x) {
		this.coordinate_x = coordinate_x;
	}

	public String getCoordinate_y() {
		return coordinate_y;
	}

	public void setCoordinate_y(String coordinate_y) {
		this.coordinate_y = coordinate_y;
	}

	public String getScore() { return score; }

	public void setScore(String score) { this.score = score; }

	public String getMagnitude() { return magnitude; }

    public void setMagnitude(String magnitude) { this.magnitude = magnitude; }

    public String getLanguage() { return language; }

    public void setLanguage(String lang) { this.language = language; }

    public String getMb_code11() { return mb_code11; }

    public void setMb_code11(String mb_code11) { this.mb_code11 = mb_code11; }

    public String getSa2_name11() { return sa2_name11; }

    public void setSa2_name11(String sa2_name11) { this.sa2_name11 = sa2_name11; }
}