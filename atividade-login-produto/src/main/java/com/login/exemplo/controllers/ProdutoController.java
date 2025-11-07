package com.login.exemplo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.login.exemplo.entity.Produto;
import com.login.exemplo.repository.ProdutoRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // 1️⃣ GET por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProdutoById(@PathVariable int id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.isPresent() ? ResponseEntity.ok(produto.get()) : ResponseEntity.notFound().build();
    }

    // 2️⃣ GET All
    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return ResponseEntity.ok(produtos);
    }

    // 3️⃣ POST (criar produto)
    @PostMapping
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoRepository.save(produto);
        return ResponseEntity.ok(novoProduto);
    }

    // 4️⃣ PUT (atualizar apenas a quantidade)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuantidade(@PathVariable int id, @RequestBody Produto produtoAtualizado) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            produto.setQuantidade(produtoAtualizado.getQuantidade());
            produtoRepository.save(produto);
            return ResponseEntity.ok(produto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5️⃣ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduto(@PathVariable int id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return ResponseEntity.ok("Produto deletado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
