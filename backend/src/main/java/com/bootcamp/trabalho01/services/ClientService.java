package com.bootcamp.trabalho01.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootcamp.trabalho01.dto.ClientDTO;
import com.bootcamp.trabalho01.entities.Client;
import com.bootcamp.trabalho01.repositories.ClientRepository;
import com.bootcamp.trabalho01.services.exceptions.DatabaseException;
import com.bootcamp.trabalho01.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllClientsPaged(PageRequest pageRequest) {
		Page<Client> listClients = repository.findAll(pageRequest);
		return listClients.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findClientById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Objeto não encontrado"));
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insertClient(ClientDTO dto) {
		Client entity = new Client();
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirth_date());
		entity.setChildren(dto.getChildren());
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO updateClient(Long id, ClientDTO dto) {
		try {
		Client entity = repository.getOne(id);
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirth_date());
		entity.setChildren(dto.getChildren());
		entity = repository.save(entity);
		return new ClientDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado: " + id);
		}
	}

	public void deleteClient(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado: " + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de integridade");
		}
	}
}
