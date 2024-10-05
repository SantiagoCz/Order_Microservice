package santiagoczarny.orders.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "processing_status")
    @Pattern(regexp = "^(Pending|Confirmed|Dispatched|Delivered|Cancelled)$", message = "The processing status is invalid")
    private String processingStatus;

    @Column(name = "item_description")
    @Size(min = 1, max = 255, message = "The item description must be between 1 and 255 characters")
    @NotBlank(message = "The item description cannot be blank")
    private String itemDescription;

    @Column(name = "customer_id")
    private Long idCustomer;

}
