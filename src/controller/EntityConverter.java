package controller;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/*
 * Esta implementacao foi baseada no caso mais comum de um converter para cada entidade, 
 * a unica diferenca e que tornamos generica para qualquer entidade, evitando-se escrever um converter para cada entidade.
 * Para mais detalhes: http://wiki.apache.org/myfaces/Entity_Converters
 * Quando clicamos no combo (SelectOneMenu) recebemos o codigo da entidade e precisamos passar
 * o objeto. Esse metodo fara isso de uma forma unica para qualquer entidade.
 */

public class EntityConverter implements Converter {  
	  
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {  
        if (value != null) {  
            return this.getAttributesFrom(component).get(value);  
        }  
        return null;  
    }  
  
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {  
  
        if (value != null  
                && !"".equals(value)) {  
  
            BaseEntity entity = (BaseEntity) value;  
  
            // adiciona item como atributo do componente  
            this.addAttribute(component, entity);  
  
            Long codigo = entity.getId();  
            if (codigo != null) {  
                return String.valueOf(codigo);  
            }  
        }  
  
        return (String) value;  
    }  
  
    protected void addAttribute(UIComponent component, BaseEntity o) {  
        String key = o.getId().toString(); // codigo como chave neste caso  
        this.getAttributesFrom(component).put(key, o);  
    }  
  
    protected Map<String, Object> getAttributesFrom(UIComponent component) {  
        return component.getAttributes();  
    }  
  
} 
