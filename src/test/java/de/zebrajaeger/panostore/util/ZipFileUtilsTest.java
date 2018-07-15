package de.zebrajaeger.panostore.util;

import org.junit.Test;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

public class ZipFileUtilsTest {
    @Test
    public void foo(){
        ZipFileUtils zfu = new ZipFileUtils();
        zfu.addPath("/foo/bar/abc/a.txt");
        assertThat(zfu.size(), is(3));
        zfu.addPath("/foo/bar/xyz/a.txt");
        assertThat(zfu.size(), is(2));
        zfu.addPath("/foo/bar/stu/a.txt");
        assertThat(zfu.size(), is(2));
        zfu.addPath("/foo/a.txt");
        assertThat(zfu.size(), is(1));
        zfu.addPath("/a.txt");
        assertThat(zfu.size(), is(0));
    }
}