package umfg.edu.comdados.restAPI.Controllers;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import umfg.edu.comdados.restAPI.Entities.ProductEntity;
import umfg.edu.comdados.restAPI.Services.ProductService;


@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/produtos")
public class ProductController{

    @Autowired
    private ProductService produtoService;

    // Endpoint para listar todos os produtos
    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProdutos() {
        List<ProductEntity> produtos = produtoService.findAll();
        return ResponseEntity.ok(produtos);
    }

    // Endpoint para obter um produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProdutoById(@PathVariable("id") Long id) {
        ProductEntity produto = produtoService.findById(id);
        if(produto == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produto);
    }

    // Endpoint para cadastrar um novo produto
    @PostMapping
    public ResponseEntity<?> createProduto(@Valid @RequestBody ProductEntity produto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        ProductEntity novoProduto = produtoService.cadastrar(produto);
        return ResponseEntity.ok(novoProduto);
    }


    // Endpoint para atualizar um produto existente
    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduto(@PathVariable("id") Long id,
                                                 @RequestBody ProductEntity produtoDetails) {
        ProductEntity produtoAtualizado = produtoService.update(id, produtoDetails);
        if(produtoAtualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produtoAtualizado);
    }

    // Endpoint para excluir um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable("id") Long id) {
        ProductEntity produto = produtoService.findById(id);
        if(produto == null){
            return ResponseEntity.notFound().build();
        }
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
