package fr.esic.gestiondestock.services.impl;

import fr.esic.gestiondestock.dto.ClientDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.model.Client;
import fr.esic.gestiondestock.model.CommandeClient;
import fr.esic.gestiondestock.repository.ClientRepository;
import fr.esic.gestiondestock.repository.CommandeClientRepository;
import fr.esic.gestiondestock.services.ClientService;
import fr.esic.gestiondestock.validator.ClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private CommandeClientRepository commandeClientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, CommandeClientRepository commandeClientRepository) {
		this.clientRepository = clientRepository;
		this.commandeClientRepository = commandeClientRepository;
	}

	@Override
    public ClientDto save(ClientDto dto) {
        List<String> errors = ClientValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("client is not valid {}", dto);
            throw new InvalidEntityException("le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
        }
        return ClientDto.fromEntity(
                clientRepository.save(
                        ClientDto.toEntity(dto)
                )
        );
    }

    @Override
    public ClientDto findById(Integer id) {
        if (id == null){
            log.error("client Id is null");
            return null;
        }
        Optional<Client> client = clientRepository.findById(id);
        ClientDto dto = ClientDto.fromEntity(client.get());
        return Optional.of(dto).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun cient avec l'ID = " + id + "n'a été trouvé",
                        ErrorCodes.CLIENT_NOT_FOUND)
        );
    }


    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientDto::fromEntity)
                .collect(Collectors.toList());
    }
    

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("client Id is null");
            return;
        }
        List<CommandeClient> commandesClients = commandeClientRepository.findAllByClientId(id);
        if (!commandesClients.isEmpty()) {
        	throw new InvalidOperationException("impossible de supprimer un client utilisé dans des commandes clients", 
        			ErrorCodes.CLIENT_ALREADY_IN_USE);
        }
        clientRepository.deleteById(id);
    }
}
