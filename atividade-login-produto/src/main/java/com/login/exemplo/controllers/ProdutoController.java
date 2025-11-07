package com.login.exemplo.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.exemplo.dto.ProdutoRequestDTO;
import com.login.exemplo.dto.ProdutoResponseDTO;
import com.login.exemplo.entity.Produto;
import com.login.exemplo.repository.ProdutoRepository;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> getAllProdutos() {
        List<ProdutoResponseDTO> produtos = produtoRepository.findAll()
                .stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/id")
    public ResponseEntity<?> getProdutoById(@PathVariable int id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.map(value -> ResponseEntity.ok(new ProdutoResponseDTO(value)))
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> createProduto(@RequestBody ProdutoRequestDTO dto) {
        Produto produto = new Produto(dto.getNome(), dto.getPreco(), dto.getQuantidade());
        Produto salvo = produtoRepository.save(produto);
        return ResponseEntity.ok(new ProdutoResponseDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduto(@PathVariable int id, @RequestBody ProdutoRequestDTO dto) {
        Optional<Produto> existente = produtoRepository.findById(id);
        if (existente.isPresent()) {
            Produto produto = existente.get();
            produto.setNome(dto.getNome());
            produto.setPreco(dto.getPreco());
            produto.setQuantidade(dto.getQuantidade());
            produtoRepository.save(produto);
            return ResponseEntity.ok(new ProdutoResponseDTO(produto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduto(@PathVariable int id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return ResponseEntity.ok("Produto deletado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
