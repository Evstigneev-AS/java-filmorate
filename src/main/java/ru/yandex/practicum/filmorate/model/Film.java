package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"name", "releaseDate"})
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Long> likes;

    @JsonIgnore
    public int getLikesCount() {
        return likes.size();
    }

    @JsonProperty("duration")
    public long getDurationInSeconds() {
        return duration != null ? duration.getSeconds() : 0;
    }

    @JsonProperty("duration")
    public void setDurationInSeconds(long durationInSeconds) {
        this.duration = Duration.ofSeconds(durationInSeconds);
    }
}