package org.paginationdemo.controller;

import java.util.Optional;

import org.paginationdemo.domain.Pager;
import org.paginationdemo.domain.Person;
import org.paginationdemo.service.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Branislav Lazic
 */
@Controller
public class PersonController {

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 5;
	private static final int[] PAGE_SIZES = { 5, 10, 20 };

	private PersonService personService;

	public PersonController(PersonService studentService) {
		this.personService = studentService;
	}

	/**
	 * Handles all requests
	 * 
	 * @param pageSize
	 * @param page
	 * @return model and view
	 */
	@GetMapping("/persons")
	public ModelAndView showPersonsPage() {
		ModelAndView modelAndView = new ModelAndView("persons");
		return modelAndView;
	}

	@GetMapping("/")
	public ModelAndView index() {
		return new ModelAndView("redirect:/persons");
	}

	@GetMapping("/persons/search")
	public ModelAndView search(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam(name = "nome", required = false) String nome) {
		ModelAndView modelAndView = new ModelAndView("/ajax-persons");
		// Evaluate page size. If requested parameter is null, return initial
		// page size
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		// Evaluate page. If requested parameter is null or less than 0 (to
		// prevent exception), return initial size. Otherwise, return value of
		// param. decreased by 1.
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		Page<Person> resultList = personService.findAllPageable(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(resultList.getTotalPages(), resultList.getNumber(), BUTTONS_TO_SHOW, "/persons/search");

		modelAndView.addObject("resultList", resultList);
		modelAndView.addObject("selectedPageSize", evalPageSize);
		modelAndView.addObject("pageSizes", PAGE_SIZES);
		modelAndView.addObject("pager", pager);
		return modelAndView;
	}
}
