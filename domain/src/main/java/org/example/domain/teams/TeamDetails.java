package org.example.domain.teams;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.example.util.ValidationUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamDetails implements Serializable {

    private static final long serialVersionUID = 1191967608407038974L;

    @NotBlank
    private String teamId;

    @NotBlank
    private String teamName;

    @NotBlank
    private String teamDesc;

    /**
     * Creating a custom builder.
     *
     * @return new custom builder.
     */
    public static TeamDetailsBuilderCustom builder() {
        return new TeamDetailsBuilderCustom();
    }

    /**
     * Make sure the object is validated upon creation using builder.
     */
    public static class TeamDetailsBuilderCustom extends TeamDetailsBuilder {

        @Override
        public TeamDetails build() {
            final TeamDetails obj = super.build();
            ValidationUtils.validate(obj);
            return obj;
        }
    }
}
