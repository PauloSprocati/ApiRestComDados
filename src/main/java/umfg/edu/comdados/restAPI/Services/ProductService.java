package umfg.edu.comdados.restAPI.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umfg.edu.comdados.restAPI.Entities.ProductEntity;
import umfg.edu.comdados.restAPI.Repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository produtoRepository;

    // Método para cadastrar um novo produto
    public ProductEntity cadastrar(ProductEntity produto) {
        return produtoRepository.save(produto);
    }

    // Método para listar todos os produtos
    public List<ProductEntity> findAll() {
        return produtoRepository.findAll();
    }

    // Buscar produto por ID
    public ProductEntity findById(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    // Atualizar produto
    public ProductEntity update(Long id, ProductEntity produtoDetails) {
        ProductEntity produto = findById(id);
        if(produto != null) {
            produto.setNome(produtoDetails.getNome());
            produto.setDescricao(produtoDetails.getDescricao());
            produto.setPreco(produtoDetails.getPreco());
            return produtoRepository.save(produto);
        }
        return null;
    }

    // Excluir produto
    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }
}
