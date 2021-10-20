package fr.esic.gestiondestock.services.impl;

import fr.esic.gestiondestock.dto.CategoryDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.model.Article;
import fr.esic.gestiondestock.repository.ArticleRepository;
import fr.esic.gestiondestock.repository.CategoryRepository;
import fr.esic.gestiondestock.services.CategoryService;
import fr.esic.gestiondestock.validator.CategoryValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ArticleRepository articleRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
		this.categoryRepository = categoryRepository;
		this.articleRepository = articleRepository;
	}

	@Override
    public CategoryDto save(CategoryDto dto) {
        List<String> errors = CategoryValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Category is not valid {}", dto);
            throw new InvalidEntityException("la Categorie n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
        }
        return CategoryDto.fromEntity(
                categoryRepository.save(
                        CategoryDto.toEntity(dto)
                )
        );
    }

    @Override
    public CategoryDto findById(Integer id) {
        if (id == null){
            log.error("Category ID is null");
            return null;
        }
        return categoryRepository.findById(id)
        		.map(CategoryDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
        				"Aucune category avec l'ID = " + id + " n'a été trouvé",
        				ErrorCodes.CATEGORY_NOT_FOUND)
        		);
//        Optional<Category> category = categoryRepository.findById(id);
//        CategoryDto dto = CategoryDto.fromEntity(category.get());
//        return Optional.of(dto).orElseThrow(() ->
//                new EntityNotFoundException(
//                        "Aucune category avec l'ID = " + id + "n'a été trouvé",
//                        ErrorCodes.CATEGORY_NOT_FOUND)
//        );
    }

    @Override
    public CategoryDto findByCode(String code) {
        if (!StringUtils.hasLength(code)){
            log.error("Category code is null");
            return null;
        }
        return categoryRepository.findCategoryByCode(code)
        		.map(CategoryDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
        				"Aucune category avec le code = " + code + " n'a été trouvé",
        				ErrorCodes.CATEGORY_NOT_FOUND)
        		);
//        Optional<Category> category = categoryRepository.findCategoryByCode(code);
//        CategoryDto dto = CategoryDto.fromEntity(category.get());
//        return Optional.of(dto).orElseThrow(() ->
//                new EntityNotFoundException(
//                        "Aucune category avec le code = " + code + " n'a été trouvé",
//                        ErrorCodes.CATEGORY_NOT_FOUND)
//        );
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Category Id is null");
            return;
        }
        List<Article> articles = articleRepository.findAllByCategoryId(id);
        if (!articles.isEmpty()) {
        	throw new InvalidOperationException("impossible de supprimer une catégorie utilisé dans des articles", 
        			ErrorCodes.CATEGORY_ALREADY_IN_USE);
        }
    	categoryRepository.deleteById(id);
    }
}
