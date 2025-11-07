package com.login.exemplo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.login.exemplo.entity.Usuario;
import com.login.exemplo.repository.UsuarioRepository;


@CrossOrigin(origins = "*")
@RestController
public class UsuarioController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	private CrudRepository<Usuario, Integer> userRepository;

	@PostMapping("cadastro")
	public ResponseEntity<Usuario> saveUser(@RequestBody Usuario user) {
	Usuario usuario = new Usuario(user.getName(), user.getEmail(), user.getPassword());
	usuarioRepository.save(usuario);
	return ResponseEntity.ok(usuario);
	}
	
	@PostMapping(value = "login")
	public ResponseEntity<?> login(@RequestBody Usuario user){
		Usuario findUser = usuarioRepository.findByEmail(user.getEmail());
		if(findUser == null) {
			return ResponseEntity.ok("Usuario não encontrado");
		} else {
			if(findUser.getPassword().equals(user.getPassword())) {
				return ResponseEntity.ok("Logado com sucesso!!");
		} else {
		    return ResponseEntity.ok("Senha incorreta");
			}
		}
	}
	
	@DeleteMapping("delete")
	public ResponseEntity<String> delete(@PathVariable Integer id){
		userRepository = null;
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Excluído com " + "sucesso!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse ID " + "não existe");
		}
	}
	
	@PutMapping("update")
	public ResponseEntity<Usuario> update(@PathVariable int id, @RequestBody Usuario newUser) {
		Optional<Usuario> ExistingUser = userRepository.findById(id);
		
		if (ExistingUser.isPresent()) {
			Usuario User = ExistingUser.get();
			User.setName(newUser.getName());
			User.setPassword(newUser.getPassword());
			userRepository.save(User);
			return ResponseEntity.ok(User);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
