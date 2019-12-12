package run.app.translator.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@Table(name = "files")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Files extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "filename", columnDefinition = "varchar(255) not null")
    private String filename;

    @Column(name = "random_name", columnDefinition = "varchar(255) not null")
    private String randomName;

    @Column(name = "path", columnDefinition = "varchar(1023) not null")
    private String path;

    /**
     * Attachment suffix,such as png, zip, mp4.
     */
    @Column(name = "suffix", columnDefinition = "varchar(50) default ''")
    private String suffix;

    /**
     * Attachment size.
     */
    @Column(name = "size", columnDefinition = "bigint not null")
    private Long size;
}
