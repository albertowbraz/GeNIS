package br.genis.servicos;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.genis.modelos.ModeloBase;

@RunWith(MockitoJUnitRunner.class)
public class ServicoPadraoTest {
    @Mock
    private EntityManager em;
    @InjectMocks
    private TestCase servicoPadrao;
    EntidadeTeste entidade;

    @Before
    public void setUp() throws Exception {
	entidade = new EntidadeTeste();
    }

    @Test
    public void testSalvarNaturalFluxos() throws Exception {
	servicoPadrao.salvar(entidade);
	Mockito.verify(em).persist(entidade);

	// Testa update
	entidade.setId(5);
	servicoPadrao.salvar(entidade);
	Mockito.verify(em).persist(entidade);
    }

    @Test(expected = ServicoBaseException.class)
    public void testSalvarLancandoException() throws ServicoBaseException {
	Mockito.doThrow(ServicoBaseException.class).when(em).persist(entidade);
	servicoPadrao.salvar(entidade);
    }
    
    @Test(expected = ServicoBaseException.class)
    @Ignore
    public void testAtualizarLancandoException() throws ServicoBaseException {
	entidade.setId(5);
	Mockito.doThrow(ServicoBaseException.class).when(em).persist(entidade);
	servicoPadrao.salvar(entidade);
    }

    @Test
    @Ignore
    public void testDeletar() throws Exception {
	throw new RuntimeException("not yet implemented");
    }

    @Test
    @Ignore
    public void testGetById() throws Exception {
	throw new RuntimeException("not yet implemented");
    }

    @Test
    @Ignore
    public void testGetAll() throws Exception {
	throw new RuntimeException("not yet implemented");
    }

    @Test
    @Ignore
    public void testBuscarPorCampos() throws Exception {
	throw new RuntimeException("not yet implemented");
    }

}

class TestCase extends ServicoPadrao<ModeloBase<?>> {

    private static boolean BOOLEAN = true;

    public static boolean isBOOLEAN() {
	return BOOLEAN;
    }

    public static void setBOOLEAN(boolean bOOLEAN) {
	BOOLEAN = bOOLEAN;
    }

    @Override
    Boolean validar(ModeloBase<?> entidade) throws ServicoBaseException {
	return BOOLEAN;
    }

}

class EntidadeTeste implements ModeloBase<Integer> {

    private static final long serialVersionUID = 3686045549036712817L;
    Integer id;

    @Override
    public Integer getId() {
	return id;
    }

    @Override
    public void setId(Integer id) {
	this.id = id;

    }

}
