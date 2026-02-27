package com.tamthong.trackr.item;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tamthong.trackr.item.record.*;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TvShowMetadata.class, name = "tvShow"),
        @JsonSubTypes.Type(value = MovieMetadata.class, name = "movie"),
        @JsonSubTypes.Type(value = MangaMetadata.class, name = "manga"),
        @JsonSubTypes.Type(value = AnimeMetadata.class, name = "anime"),
        @JsonSubTypes.Type(value = BookMetadata.class, name = "book"),
        @JsonSubTypes.Type(value = GameMetadata.class, name = "game"),
        @JsonSubTypes.Type(value = MusicMetadata.class, name = "music"),
        @JsonSubTypes.Type(value = FoodPlacesMetadata.class, name = "food"),
        @JsonSubTypes.Type(value = OtherMetadata.class, name = "other")
})
public interface ItemMetadata extends Serializable {
}
