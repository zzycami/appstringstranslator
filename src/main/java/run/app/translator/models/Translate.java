package run.app.translator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@Table(name = "translate")
@Data
@EqualsAndHashCode(callSuper = true)
public class Translate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "key", columnDefinition = "varchar(255)")
    private String key;

    @Column(name = "value", columnDefinition = "text")
    private String value;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "strings_id", referencedColumnName = "id")
    private Strings strings;
}
