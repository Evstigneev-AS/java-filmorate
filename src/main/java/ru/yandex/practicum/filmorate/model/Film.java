package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder(toBuilder = true)
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    @JsonIgnore
    private Duration duration;

    @JsonProperty("duration")
    public long getDurationInSeconds() {
        return duration != null ? duration.getSeconds() : 0;
    }

    @JsonProperty("duration")
    public void setDurationInSeconds(long durationInSeconds) {
        this.duration = Duration.ofSeconds(durationInSeconds);
    }
}