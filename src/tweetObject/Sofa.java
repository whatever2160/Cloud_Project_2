package tweetObject;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@TypeDiscriminator("doc.type === 'EligibleCouple'")

public class Sofa extends CouchDbDocument {

        @JsonProperty

       private String name;

    public Sofa() {

     }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

  


   }