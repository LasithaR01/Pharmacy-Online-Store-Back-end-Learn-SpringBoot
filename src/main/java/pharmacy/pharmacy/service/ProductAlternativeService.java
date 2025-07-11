package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.ProductAlternativeRepository;
import pharmacy.pharmacy.dao.ProductRepository;
import pharmacy.pharmacy.dto.ProductAlternativeDTO;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.entity.ProductAlternative;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductAlternativeService {

    private final ProductAlternativeRepository productAlternativeRepository;
    private final ProductRepository productRepository;

    public ProductAlternativeService(ProductAlternativeRepository productAlternativeRepository,
                                  ProductRepository productRepository) {
        this.productAlternativeRepository = productAlternativeRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductAlternativeDTO> getAllAlternatives() {
        try {
            return productAlternativeRepository.findAll().stream()
                    .map(EntityDtoMapper::convertToProductAlternativeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all product alternatives", e);
        }
    }

    @Transactional(readOnly = true)
    public ProductAlternativeDTO getAlternativeById(int id) {
        try {
            ProductAlternative alternative = productAlternativeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product alternative not found with id: " + id));
            return EntityDtoMapper.convertToProductAlternativeDTO(alternative);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve product alternative with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductAlternativeDTO> getAlternativesForProduct(int productId) {
        try {
            return productAlternativeRepository.findByProductId(productId).stream()
                    .map(EntityDtoMapper::convertToProductAlternativeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve alternatives for product id: " + productId, e);
        }
    }

    public ProductAlternativeDTO createAlternative(ProductAlternativeDTO alternativeDTO) {
        try {
            // Validate input
            if (alternativeDTO.getProductId() == alternativeDTO.getAlternativeProductId()) {
                throw new GlobalException("A product cannot be an alternative to itself");
            }

            // Check if relationship already exists
            if (productAlternativeRepository.existsByProductIdAndAlternativeProductId(
                    alternativeDTO.getProductId(), alternativeDTO.getAlternativeProductId())) {
                throw new GlobalException("This alternative relationship already exists");
            }

            // Get product entities
            Product product = productRepository.findById(alternativeDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + alternativeDTO.getProductId()));

            Product alternativeProduct = productRepository.findById(alternativeDTO.getAlternativeProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Alternative product not found with id: " + alternativeDTO.getAlternativeProductId()));

            // Create new alternative
            ProductAlternative alternative = EntityDtoMapper.convertToProductAlternative(
                    alternativeDTO, product, alternativeProduct);

            ProductAlternative savedAlternative = productAlternativeRepository.save(alternative);
            return EntityDtoMapper.convertToProductAlternativeDTO(savedAlternative);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create product alternative", e);
        }
    }

    public ProductAlternativeDTO updateAlternative(int id, ProductAlternativeDTO alternativeDTO) {
        try {
            ProductAlternative existingAlternative = productAlternativeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product alternative not found with id: " + id));

            // Only reason can be updated - product and alternative product references cannot be changed
            if (alternativeDTO.getReason() != null) {
                existingAlternative.setReason(alternativeDTO.getReason());
            }

            ProductAlternative updatedAlternative = productAlternativeRepository.save(existingAlternative);
            return EntityDtoMapper.convertToProductAlternativeDTO(updatedAlternative);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update product alternative with id: " + id, e);
        }
    }

    public void deleteAlternative(int id) {
        try {
            if (!productAlternativeRepository.existsById(id)) {
                throw new ResourceNotFoundException("Product alternative not found with id: " + id);
            }
            productAlternativeRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete product alternative with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductAlternativeDTO> getRecommendedAlternatives(int productId) {
        try {
            // Get all alternatives for the product
            List<ProductAlternative> alternatives = productAlternativeRepository.findByProductId(productId);

            // Filter and prioritize alternatives that are in stock, same category, and cheaper
            return alternatives.stream()
                    .filter(ProductAlternative::isInStock)
                    .filter(ProductAlternative::isSameCategory)
                    .filter(ProductAlternative::isCheaperAlternative)
                    .map(EntityDtoMapper::convertToProductAlternativeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to get recommended alternatives for product id: " + productId, e);
        }
    }
}