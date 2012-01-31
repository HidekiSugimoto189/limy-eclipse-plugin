/*
 * Created 2006/11/24
 * Copyright (C) 2003-2009  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of Limy Eclipse Plugin.
 *
 * Limy Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Limy Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Limy Eclipse Plugin.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.limy.velocity.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.context.Context;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * XML��Velocity�R���e�L�X�g�Ɋւ��郆�[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class XmlContextUtils {
    
    /**
     * private constructor
     */
    private XmlContextUtils() {
        // empty
    }

    /**
     * �G�������g�̓��e���R���e�L�X�g�ɒǉ����܂��B
     * @param context �R���e�L�X�g
     * @param el �G�������g
     */
    public static void addValue(Context context, Element el) {
        
        String nodeName = el.getNodeName();

        NodeList childNodes = el.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                
                // �G�������g�p�̒l�i�[�ꏊ���쐬
                Map<String, Object> values = new HashMap<String, Object>();
                // �쐬�������ɃG�������g�̏����l�߂�
                addElement(values, (Element)node);
                
                // �R���e�L�X�g�ɃG�������g���e���Z�b�g
                Object value = context.get(nodeName);
                if (value == null) {
                    context.put(nodeName, values);
                } else if (value instanceof List) {
                    ((List)value).add(values);
                    context.put(nodeName, value);
                } else {
                    for (Entry<String, Object> entry : values.entrySet()) {
                        
                        addEntry((Map)value, entry);
                    }
                    context.put(nodeName, value);
                }
            }
        }
        
        addAttrDirect((Map<String, Object>)context.get(nodeName), el); // ���[�g�v�f�̓��e���R���e�L�X�g�ɒǉ�

    }

    // ------------------------ Private Methods

    /**
     * �}�b�v��Entry�̓��e��S�Ēǉ����܂��B
     * @param value �}�b�v
     * @param entry Entry 
     */
    private static void addEntry(Map<String, Object> value, Entry<String, Object> entry) {
        
        String entryKey = entry.getKey();
        if (value.get(createMultipleName(entryKey)) != null) {
            List<Object> orgList = (List)value.get(createMultipleName(entryKey));
            orgList.add(entry.getValue());
        } else {
            Object orgValue = value.get(entryKey);
            if (orgValue == null) {
                value.put(entryKey, entry.getValue());
            } else if (orgValue instanceof List) {
                ((List)orgValue).add(entry.getValue());
            } else {
                List<Object> newList = new ArrayList<Object>();
                newList.add(orgValue);
                newList.add(entry.getValue());
                Map<String, Object> newMap = new HashMap<String, Object>();
                newMap.put(createMultipleName(entryKey), newList);
                value.remove(entryKey);
                value.put(createMultipleName(entryKey), newList);
            }
        }
    }

    /**
     * �G�������g�̏���l�}�b�v�ɋl�߂܂��B
     * @param parentValues �l�}�b�v
     * @param el �G�������g
     */
    private static void addElement(Map<String, Object> parentValues, Element el) {
        String nodeName = el.getNodeName();
        NodeList childNodes = el.getChildNodes();
        
        addAttrValues(parentValues, el);
        
        boolean enableElement = false;
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (!enableElement && node instanceof Text) {
                if (parentValues.get(nodeName) == null) {
                    parentValues.put(nodeName, node.getNodeValue());
                } else {
                    Map<String, Object> values = (Map<String, Object>)parentValues.get(nodeName);
                    // �����l�Ǝq�v�f�iAnonymous�e�L�X�g�j�������ɑ��݂���ꍇ�A�Œ�l"anontext"�Ƃ��Ďq�v�f��ǉ�
                    values.put("anontext", node.getNodeValue());
                }
            }
            if (node instanceof Element) {
                enableElement = true;
                Map<String, Object> subValues = new HashMap<String, Object>();
                addElement(subValues, (Element)node);
                
                if (parentValues.get(nodeName) instanceof String) {
                    parentValues.put(nodeName, subValues);
                } else if (parentValues.get(nodeName) instanceof List) {
                    addChildValue(parentValues, nodeName, subValues);
                } else {
                    addChildValue(parentValues, nodeName, subValues, node.getNodeName());
                }
            }
        }
    }
    /**
     * �v�f�̑S������ values �ɒǉ����܂��i�����ǉ��Łj�B
     * @param values �l�i�[��}�b�v
     * @param el �v�f
     */
    private static void addAttrDirect(Map<String, Object> values, Element el) {
        NamedNodeMap attrs = el.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            values.put(attr.getNodeName(), attr.getNodeValue());
        }
    }
    
    /**
     * �v�f�̑S������ values �ɒǉ����܂��i�T�u�}�b�v�쐬�Łj�B
     * @param values �l�i�[��}�b�v
     * @param el �v�f
     */
    private static void addAttrValues(Map<String, Object> values, Element el) {
        NamedNodeMap attrs = el.getAttributes();
        if (attrs.getLength() > 0) {
            Map<String, Object> subValues = new HashMap<String, Object>();
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                subValues.put(attr.getNodeName(), attr.getNodeValue());
            }
            
            String nodeName = el.getNodeName();
            if (values.get(nodeName) == null) {
                values.put(nodeName, subValues);
            } else {
                throw new IllegalStateException("�\������Ă��Ȃ��`����XML�t�@�C���ł��B");
            }
        }
    }

    /**
     * �e�̒l�}�b�v�Ɏq�̒l��ǉ����܂��B
     * @param parentValues �e�̒l�}�b�v
     * @param nodeName �e�G�������g��
     * @param subValues �q�̒l�}�b�v
     */
    private static void addChildValue(Map<String, Object> parentValues,
            String nodeName, Map<String, Object> subValues) {
        
        // �q�̐擪�l���擾
        Object subValueFirst = subValues.values().toArray()[0];
//        if (subValueFirst instanceof String) {
//            // ������̏ꍇ�A�e�̒l�}�b�v�Ɏq�̒l��ǉ�
//            List<String> parentValue = (List<String>)(parentValues.get(nodeName));
//            parentValue.add((String)subValueFirst);
//            
//        } else {
            // �}�b�v�̏ꍇ�A�e�̒l�}�b�v�Ɏq�̒l��ǉ�
            List<Map<String, Object>> parentValue = (List<Map<String, Object>>)
                    (parentValues.get(nodeName));
            parentValue.add((Map<String, Object>)subValueFirst);
//        }
    }

    

    /**
     * �e�̒l�}�b�v�Ɏq�̒l��ǉ����܂��B
     * @param parentValues �e�̒l�}�b�v
     * @param parentName �e�G�������g��
     * @param subValues �q�̒l�}�b�v
     * @param childName �q�G�������g��
     */
    private static void addChildValue(Map<String, Object> parentValues,
            String parentName, Map<String, Object> subValues, String childName) {
        
        Map<String, Object> orgValues = (Map<String, Object>)parentValues.get(parentName);
        if (orgValues == null) {
            parentValues.put(parentName, subValues);
        } else {
            for (Entry<String, Object> entry : subValues.entrySet()) {
                
                String entryKey = entry.getKey();
                if (orgValues.get(createMultipleName(entryKey)) != null) {
                    List orgList = (List)orgValues.get(createMultipleName(entryKey));
                    orgList.add(entry.getValue());
                } else if (orgValues.get(entryKey) != null) {
                    Map<String, Object> mapByParent = (Map<String, Object>)
                            parentValues.get(parentName);
                    Object v = mapByParent.get(childName);
                    List result = makeList(v, subValues.values().toArray()[0]);
                    
                    //((Map)parentValues.get(parentName)).keySet().iterator().next() // error
                    mapByParent.remove(childName);
                    mapByParent.put(createMultipleName(childName), result);
//                    parentValues.put(parentName, result);

                    break;
                } else {
                    orgValues.put(entryKey, entry.getValue());
                }
            }
        }
    }

    /**
     * �����̒l�ƁA����ǉ�����l�����X�g�ɋl�߂ĕԂ��܂��B
     * @param orgValue �����̒l
     * @param nowValue ����ǉ�����l
     * @return �����̒l�ƍ���ǉ�����l���l�߂����X�g
     */
    private static List makeList(Object orgValue, Object nowValue) {
        
        if (orgValue instanceof List) {
            ((List)orgValue).add(nowValue);
            return (List)orgValue;
        } else if (orgValue instanceof String) {
            List<String> list = new ArrayList<String>();
            list.add((String)orgValue);
            list.add((String)nowValue);
            return list;
        } else {
            Map<String, Object> mapValue = (Map<String, Object>)orgValue;
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            list.add(mapValue);
            list.add((Map<String, Object>)nowValue);
            return list;
        }
    }

    /**
     * ���̂𕡐��p���̂ɂ��ĕԂ��܂��B
     * @param name ����
     * @return �����p����
     */
    private static String createMultipleName(String name) {
        return name + "s";
    }

}
