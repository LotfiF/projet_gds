package fr.esic.gestiondestock.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fr.esic.gestiondestock.dto.CategoryDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.services.CategoryService;

@RunWith(SpringRunner.class)
@SpringBootTest
class CategoryServiceImplTest {
	
	@Autowired
	private CategoryService service;

	@Test
	public void shouldSaveCategoryWithSuccess() {
		CategoryDto expectedCategory = CategoryDto.builder()
				.code("cat test")
				.designation("Designation test")
				.idEntreprise(1)
				.build();
		
		CategoryDto savedCategory = service.save(expectedCategory);
		
		assertNotNull(savedCategory);
		assertNotNull(savedCategory.getId());
		assertEquals(expectedCategory.getCode(), savedCategory.getCode());
		assertEquals(expectedCategory.getDesignation(), savedCategory.getDesignation());
		assertEquals(expectedCategory.getIdEntreprise(), savedCategory.getIdEntreprise());
	}
	
	@Test
	public void shouldUpdateCategoryWithSuccess() {
		CategoryDto expectedCategory = CategoryDto.builder()
				.code("cat test")
				.designation("Designation test")
				.idEntreprise(1)
				.build();
		
		CategoryDto savedCategory = service.save(expectedCategory);
		
		CategoryDto updatedCategory = savedCategory;
		updatedCategory.setCode("cas update");
		
		savedCategory = service.save(updatedCategory);
		
		assertNotNull(updatedCategory);
		assertNotNull(updatedCategory.getId());
		assertEquals(updatedCategory.getCode(), savedCategory.getCode());
		assertEquals(updatedCategory.getDesignation(), savedCategory.getDesignation());
		assertEquals(updatedCategory.getIdEntreprise(), savedCategory.getIdEntreprise());
	}
	
	@Test
	public void shouldThrowInvalidEntityException() {
		CategoryDto expectedCategory = CategoryDto.builder().build();
		
		InvalidEntityException expectedException = assertThrows(InvalidEntityException.class, ()-> service.save(expectedCategory));
		
		assertEquals(ErrorCodes.CATEGORY_NOT_VALID, expectedException.getErrorCode());
		assertEquals(1, expectedException.getErrors().size());
		assertEquals("Veuillez renseigner le code de la categorie", expectedException.getErrors().get(0));

	}
	
	@Test
	public void shouldThrowEntityNotFoundException() {
		EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> service.findById(0));

		assertEquals(ErrorCodes.CATEGORY_NOT_FOUND, expectedException.getErrorCode());
		assertEquals("Aucune category avec l'ID = 0 n'a été trouvé", expectedException.getMessage());
	}

//	@Test(expected = EntityNotFoundException.class)
//	public void shouldThrowEntityNotFoundException2() {
//		service.findById(0);
//	}

//	@Test
//	void testFindById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testFindByCode() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testFindAll() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDelete() {
//		fail("Not yet implemented");
//	}

}
