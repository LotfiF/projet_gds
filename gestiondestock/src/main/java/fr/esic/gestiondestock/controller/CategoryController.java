package fr.esic.gestiondestock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import fr.esic.gestiondestock.controller.api.CategoryApi;
import fr.esic.gestiondestock.dto.CategoryDto;
import fr.esic.gestiondestock.services.CategoryService;

@RestController
public class CategoryController implements CategoryApi {
	
	private CategoryService categoryService;
	
	@Autowired
	private CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Override
	public CategoryDto save(CategoryDto dto) {
		// TODO Auto-generated method stub
		return categoryService.save(dto);
	}

	@Override
	public CategoryDto findById(Integer idCategory) {
		// TODO Auto-generated method stub
		return categoryService.findById(idCategory);
	}

	@Override
	public CategoryDto findByCode(String code) {
		// TODO Auto-generated method stub
		return categoryService.findByCode(code);
	}

	@Override
	public List<CategoryDto> findAll() {
		// TODO Auto-generated method stub
		return categoryService.findAll();
	}

	@Override
	public void delete(Integer id) {
		categoryService.delete(id);
		
	}

}
